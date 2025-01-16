package com.flux.flux.v1.message;

import com.flux.flux.v1._shared.websocket.dto.WebSocketMessage;
import com.flux.flux.v1.message.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageWebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void notifyMessageCreated(MessageDTO message, Long channelId) {
        WebSocketMessage wsMessage = WebSocketMessage.builder("MESSAGE_CREATE")
                .add("id", message.id())
                .add("content", message.content())
                .add("created_at", message.createdAt())
                .add("last_modified_at", message.lastModifiedAt())
                .add("channelId", channelId)
                .build();

        send(channelId, wsMessage);
    }

    public void notifyMessageUpdated(MessageDTO message, Long channelId) {
        WebSocketMessage wsMessage = WebSocketMessage.builder("MESSAGE_UPDATE")
                .add("id", message.id())
                .add("content", message.content())
                .add("created_at", message.createdAt())
                .add("last_modified_at", message.lastModifiedAt())
                .add("channelId", channelId)
                .build();

        send(channelId, wsMessage);
    }

    public void notifyMessageDeleted(Long messageId, Long channelId) {
        WebSocketMessage wsMessage = WebSocketMessage.builder("MESSAGE_DELETE")
                .add("id", messageId)
                .add("channelId", channelId)
                .build();

        send(channelId, wsMessage);
    }

    private void send(Long channelId, WebSocketMessage message) {
        messagingTemplate.convertAndSend(
                "/topic/channels/" + channelId + "/messages",
                message
        );
    }
}
