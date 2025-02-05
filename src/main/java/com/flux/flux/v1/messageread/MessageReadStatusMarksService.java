package com.flux.flux.v1.messageread;

import com.flux.flux.v1.message.Message;
import com.flux.flux.v1.message.MessageService;
import com.flux.flux.v1.user.User;
import com.flux.flux.v1.user.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageReadStatusMarksService {
    private final MessageService messageService;
    private final MessageReadStatusRepository messageReadStatusRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageReadStatusService messageReadStatusService;

    @Transactional
    public void bulkRead(Long channelId, MessageBulkReadDTO messageBulkReadDTO, Principal principal) {
        User user = userDetailsServiceImpl.loadUserByUsername(principal.getName());
        Set<Message> messages =
                messageService.findMessagesByIdsAndChannelId(messageBulkReadDTO.messageIds(), channelId);

        Set<MessageReadStatus> userMessagesReadStatuses =
                messageReadStatusService.findReadStatusesByMessageIdsAndUserId(messageBulkReadDTO.messageIds(), user.getId());

        userMessagesReadStatuses.forEach(status -> {
            messages.removeIf(message -> message.getId().equals(status.getMessageId()));
        });

        if (!messages.isEmpty()) {
            long authorId = messages.stream().findFirst().map((message -> message.getAuthor().getId())).get();

            Set<MessageReadStatus> messageStatusesToSave = messages.stream()
                    .map(message -> MessageReadStatus.builder()
                            .userId(user.getId())
                            .messageId(message.getId())
                            .build())
                    .collect(Collectors.toSet());

            messageReadStatusRepository.saveAll(messageStatusesToSave);

            sendReadStatusToMessageAuthor(authorId, channelId, messages);
        }
    }

    private void sendReadStatusToMessageAuthor(Long authorId, Long channelId, Set<Message> messages) {
        messagingTemplate.convertAndSendToUser(
                authorId.toString(),
                "/queue/channels/" + channelId + "/messages",
                messages
        );
    }
}
