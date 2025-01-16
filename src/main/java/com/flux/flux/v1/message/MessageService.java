package com.flux.flux.v1.message;

import com.flux.flux.v1.channel.Channel;
import com.flux.flux.v1.channel.ChannelService;
import com.flux.flux.v1.channel.enumeration.ChannelType;
import com.flux.flux.v1.hubs.Hub;
import com.flux.flux.v1.hubs.HubService;
import com.flux.flux.v1.message.dto.CreateMessageDTO;
import com.flux.flux.v1.message.dto.MessageDTO;
import com.flux.flux.v1.message.dto.UpdateMessageDTO;
import com.flux.flux.v1.message.event.MessageCreatedEvent;
import com.flux.flux.v1.message.event.MessageDeletedEvent;
import com.flux.flux.v1.message.event.MessageUpdatedEvent;
import com.flux.flux.v1.permission.PermissionService;
import com.flux.flux.v1.permission.enumeration.Permission;
import com.flux.flux.v1.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final ChannelService channelService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final PermissionService permissionService;
    private final HubService hubService;
    private final ApplicationEventPublisher eventPublisher;

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

        MessageDTO newMessageDTO = messageMapper.toDTO(messageRepository.save(message));

        eventPublisher.publishEvent(new MessageCreatedEvent(newMessageDTO, channelId));

        return newMessageDTO;
    }

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
}
