package com.flux.flux.v1.message;

import com.flux.flux.v1.message.event.MessageCreatedEvent;
import com.flux.flux.v1.message.event.MessageDeletedEvent;
import com.flux.flux.v1.message.event.MessageUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageEventListener {
    private final MessageWebSocketService messageWebSocketService;

    @EventListener
    public void handleMessageCreated(MessageCreatedEvent event) {
        messageWebSocketService.notifyMessageCreated(event.message(), event.channelId());
    }

    @EventListener
    public void handleMessageUpdated(MessageUpdatedEvent event) {
        messageWebSocketService.notifyMessageUpdated(event.message(), event.channelId());
    }

    @EventListener
    public void handleMessageDeleted(MessageDeletedEvent event) {
        messageWebSocketService.notifyMessageDeleted(event.messageId(), event.channelId());
    }
}
