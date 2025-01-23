package com.flux.flux.v1.message;

import com.flux.flux.v1.channel.Channel;
import com.flux.flux.v1.channel.ChannelService;
import com.flux.flux.v1.channel.enumeration.ChannelType;
import com.flux.flux.v1.hubs.Hub;
import com.flux.flux.v1.hubs.HubService;
import com.flux.flux.v1.message.dto.CreateMessageDTO;
import com.flux.flux.v1.message.dto.GetMessagesFilter;
import com.flux.flux.v1.message.dto.MessageDTO;
import com.flux.flux.v1.message.dto.UpdateMessageDTO;
import com.flux.flux.v1.message.event.MessageCreatedEvent;
import com.flux.flux.v1.message.event.MessageDeletedEvent;
import com.flux.flux.v1.message.event.MessageUpdatedEvent;
import com.flux.flux.v1.messageread.MessageReadStatusService;
import com.flux.flux.v1.permission.PermissionService;
import com.flux.flux.v1.permission.enumeration.Permission;
import com.flux.flux.v1.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final ChannelService channelService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final PermissionService permissionService;
    private final HubService hubService;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageReadStatusService messageReadStatusService;

    @Transactional
    public MessageDTO create(Long channelId, CreateMessageDTO createMessageDTO, User currentUser) {
        Channel channel = channelService.findChannelById(channelId);

        if (channel.getType() == ChannelType.VOICE || channel.getType() == ChannelType.TEXT) {
            Hub hub = hubService.findHubById(currentUser.getId());
            permissionService.hasPermissionsThrow(currentUser.getId(), hub.getId(), Permission.SEND_MESSAGES);
        }

        Message message = Message.builder()
                .content(createMessageDTO.content())
                .author(currentUser)
                .channel(channel)
                .build();

        MessageDTO newMessageDTO;

        if (channel.getType() == ChannelType.DC) {
            newMessageDTO = messageMapper.toDTO(messageRepository.save(message), false);
        } else if (channel.getType() == ChannelType.GROUP_DC || channel.getType() == ChannelType.TEXT) {
            newMessageDTO = messageMapper.toDTO(messageRepository.save(message), false, 0);
        } else {
            newMessageDTO = messageMapper.toDTO(messageRepository.save(message));
        }

        eventPublisher.publishEvent(new MessageCreatedEvent(newMessageDTO, channelId));

        return newMessageDTO;
    }

    // TODO: НОРМАЛИЗОВАТЬ СТАТУС ПРОЧТЕНИЯ ПРИ ВЫВОДЕ ПОСЛЕ АПДЕЙТА
    @Transactional
    public MessageDTO update(Long channelId, Long messageId, UpdateMessageDTO updateMessageDTO, User currentUser) {
        Message message = findMessageById(messageId);

        if (!message.getChannel().getId().equals(channelId) ||
                !message.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Permission denied");
        }

        boolean hasChanges = !message.getContent().equals(updateMessageDTO.content());

        if (!hasChanges) {
            return messageMapper.toDTO(message);
        }

        message.setContent(updateMessageDTO.content());

        Message savedMessage = messageRepository.save(message);
        MessageDTO updatedMessageDTO = messageMapper.toDTO(savedMessage);

        eventPublisher.publishEvent(new MessageUpdatedEvent(updatedMessageDTO, channelId));

        return updatedMessageDTO;
    }

    @Transactional(readOnly = true)
    public Message findMessageById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));
    }

    @Transactional
    public void delete(Long channelId, Long messageId, User currentUser) {
        Message message = findMessageById(messageId);

        if (!message.getChannel().getId().equals(channelId) ||
                !message.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Permission denied");
        }

        Hub hub = hubService.findHubByChannelId(message.getChannel().getId());

        if (message.getChannel().getType() == ChannelType.VOICE || message.getChannel().getType() == ChannelType.TEXT) {
            permissionService.hasPermissionsThrow(currentUser.getId(), hub.getId(), Permission.SEND_MESSAGES);
        }

        messageRepository.delete(message);

        eventPublisher.publishEvent(new MessageDeletedEvent(messageId, channelId));
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> getChannelMessages(Long channelId, User currentUser, GetMessagesFilter filter) {
        Channel channel = channelService.findChannelById(channelId);

        Pageable pageable = PageRequest.of(0, filter.getSize(), Sort.Direction.DESC, "id");

        List<Message> messages;

        if (filter.getBefore() != null && filter.getBefore() > 0) {
            messages = messageRepository.findAllByChannelIdAndBeforeId(channelId, filter.getBefore(), pageable);
        } else if (filter.getAfter() != null && filter.getAfter() > 0) {
            messages = messageRepository.findAllByChannelIdAndAfterId(channelId, filter.getAfter(), pageable);
        } else if (filter.getAround() != null && filter.getAround() > 0) {
            messages = messageRepository.findAllByChannelIdAndAroundId(channelId, filter.getAround(), pageable);
        } else {
            messages = messageRepository.findAllByChannelId(channelId, pageable);
        }

        return switch (channel.getType()) {
            case DC -> enrichDirectMessages(messages, currentUser);
            case GROUP_DC, TEXT -> enrichGroupDirectAndTextMessagesWithStatus(messages, currentUser);
            default -> {
                log.error("Unsupported channel type for id {}", channel.getId());
                throw new IllegalAccessError("Unsupported channel type");
            }
        };

    }

    private List<MessageDTO> enrichDirectMessages(List<Message> messages, User currentUser) {
        List<Long> messageIds = messages.stream().map(Message::getId).toList();

        Set<Long> readStatuses =
                messageReadStatusService.findReadStatusesByMessageIdsAndUserId(messageIds, currentUser.getId())
                        .stream()
                        .map(messageReadStatus -> messageReadStatus.getMessage().getId())
                        .collect(Collectors.toSet());

        return messages.stream()
                .map(message -> {
                    if (message.getAuthor().getId().equals(currentUser.getId())) {
                        return messageMapper.toDTO(message, readStatuses.contains(message.getId()));
                    }

                    return messageMapper.toDTO(message);
                })
                .toList();
    }

    private List<MessageDTO> enrichGroupDirectAndTextMessagesWithStatus(List<Message> messages, User currentUser) {
        List<Long> messageIds = messages.stream().map(Message::getId).toList();

        Map<Long, Long> readStatusCount = messageReadStatusService.countReadStatusesByMessageIds(messageIds);

        return messages.stream()
                .map(message -> {
                    if (message.getAuthor().getId().equals(currentUser.getId())) {
                        long count = readStatusCount.get(message.getId()) == null ? 0 : readStatusCount.get(message.getId());
                        return messageMapper.toDTO(message, count > 0, count);
                    }

                    return messageMapper.toDTO(message);
                })
                .toList();
    }
}
