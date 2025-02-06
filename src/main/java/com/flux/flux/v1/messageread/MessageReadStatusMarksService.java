package com.flux.flux.v1.messageread;

import com.flux.flux.v1._shared.websocket.dto.WebSocketMessage;
import com.flux.flux.v1.message.MessageService;
import com.flux.flux.v1.user.User;
import com.flux.flux.v1.user.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageReadStatusMarksService {
    private final MessageService messageService;
    private final MessageReadStatusRepository messageReadStatusRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void bulkRead(Long channelId, MessageBulkReadDTO messageBulkReadDTO, Principal principal) {
        User user = userDetailsServiceImpl.loadUserByUsername(principal.getName());
        Set<Long> unreadMessageIds =
                messageService.findUnreadMessagesInChannelByIdsForUser(user.getId(), messageBulkReadDTO.messageIds(), channelId);

        markAsRead(channelId, unreadMessageIds, user);
    }

    @Transactional
    public void bulkReadAll(Long channelId, Principal principal) {
        User user = userDetailsServiceImpl.loadUserByUsername(principal.getName());
        Set<Long> unreadMessageIds =
                messageService.findUnreadMessageIdsInChannelForUser(user.getId(), channelId);

        markAsRead(channelId, unreadMessageIds, user);
    }

    private void markAsRead(Long channelId, Set<Long> unreadMessageIds, User user) {
        if (!unreadMessageIds.isEmpty()) {
            Set<Long> authorIds = messageService.findAuthorIdsByMessageIds(unreadMessageIds);

            Set<Long> authors = messageService.findAuthorIdsByMessageIds(unreadMessageIds);

            Set<MessageReadStatus> messageStatusesToSave = unreadMessageIds.stream()
                    .map(messageId -> MessageReadStatus.builder()
                            .userId(user.getId())
                            .messageId(messageId)
                            .build())
                    .collect(Collectors.toSet());

            messageReadStatusRepository.saveAll(messageStatusesToSave);

            authorIds.forEach(authorId -> {
                sendReadStatusToMessageAuthor(authorId, channelId, unreadMessageIds);
            });
        }
    }

    private void sendReadStatusToMessageAuthor(Long authorId, Long channelId, Set<Long> messageIds) {
        Map<String, Object> messageRange = new HashMap<>();
        messageRange.put("from", messageIds.stream().min(Long::compareTo).get());
        messageRange.put("to", messageIds.stream().max(Long::compareTo).get());

        WebSocketMessage wsMessage =  WebSocketMessage.builder("MESSAGE_READ_STATUS")
                .add("message_range", messageRange)
                .add("channel_id", channelId)
                .build();

        messagingTemplate.convertAndSendToUser(
                authorId.toString(),
                "/queue/channels/" + channelId + "/messages",
                wsMessage
        );
    }
}
