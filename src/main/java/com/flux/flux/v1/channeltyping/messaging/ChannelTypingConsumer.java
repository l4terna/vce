package com.flux.flux.v1.channeltyping.messaging;

import com.flux.flux.v1.channeltyping.ChannelTypingWebSocketService;
import com.flux.flux.v1.channeltyping.event.ChannelTypingStatusChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelTypingConsumer {
    private final ChannelTypingWebSocketService channelTypingWebSocketService;

    @KafkaListener(topics = "channel.typing", groupId = "channel-service.typing-processor.typing")
    public void handleTypingStatus(ChannelTypingStatusChangeEvent event) {
        channelTypingWebSocketService.sendChannelTypingChangedMessage(event);
    }
}
