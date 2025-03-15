package com.flux.flux.v1.channeltyping;

import com.flux.flux.v1._shared.websocket.dto.WebSocketMessage;
import com.flux.flux.v1.channeltyping.enumeration.ChannelTypingStatus;
import com.flux.flux.v1.channeltyping.event.ChannelTypingStatusChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ChannelTypingWebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendChannelTypingChangedMessage(ChannelTypingStatusChangeEvent event) {
        WebSocketMessage.WebSocketMessageBuilder wsmb;

        if (event.channelTypingStatus() == ChannelTypingStatus.STARTED) {
            wsmb = WebSocketMessage.builder("TYPING_STARTED");
        } else {
            wsmb = WebSocketMessage.builder("TYPING_STOPPED");
        }

        WebSocketMessage wsm = wsmb
                .add("channelId", event.channelId())
                .add("user", event.user())
                .build();

        messagingTemplate.convertAndSend(
                "/v1/topic/channels/" + event.channelId() + "/typing",
                wsm
        );
    }
}
