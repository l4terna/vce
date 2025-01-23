package com.flux.flux.v1.message;

import com.flux.flux.v1._shared.websocket.dto.WebSocketMessage;
import com.flux.flux.v1.message.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MessageWebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void messageCreated(MessageDTO message, Long channelId) {
//        for (Long memberId : channelMemberIds) {
//            boolean isRead = memberId.equals(message.author().id());

            WebSocketMessage wsMessage = WebSocketMessage.builder("MESSAGE_CREATE")
                    .add("id", message.id())
                    .add("author", message.author())
                    .add("content", message.content())
                    .add("created_at", message.createdAt())
                    .add("channelId", channelId)
                    .build();

            send(channelId, wsMessage);

//            messagingTemplate.convertAndSendToUser(
//                    memberId.toString(),
//                    "/topic/channels/" + channelId + "/messages",
//                    wsMessage
//            );
//        }
    }

    public void messageUpdated(MessageDTO message, Long channelId) {
        WebSocketMessage wsMessage = WebSocketMessage.builder("MESSAGE_UPDATE")
                .add("id", message.id())
                .add("author", message.author())
                .add("content", message.content())
                .add("created_at", message.createdAt())
                .add("channelId", channelId)
                .build();

        send(channelId, wsMessage);
    }

    public void messageDeleted(Long messageId, Long channelId) {
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
