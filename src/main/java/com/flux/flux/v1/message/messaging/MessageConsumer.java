package com.flux.flux.v1.message.messaging;

import com.flux.flux.v1.message.MessageWebSocketService;
import com.flux.flux.v1.message.event.MessageCreatedEvent;
import com.flux.flux.v1.message.event.MessageDeletedEvent;
import com.flux.flux.v1.message.event.MessageUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final MessageWebSocketService messageWebSocketService;

    @KafkaListener(topics = "channel.messages.created", groupId = "channel-service.message-processor.create")
    public void consumeCreate(MessageCreatedEvent event) {
        messageWebSocketService.messageCreated(event.message());
    }

    @KafkaListener(topics = "channel.messages.updated", groupId = "channel-service.message-processor.update")
    public void consumeUpdate(MessageUpdatedEvent event) {
        messageWebSocketService.messageUpdated(event.message());
    }

    @KafkaListener(topics = "channel.messages.deleted", groupId = "channel-service.message-processor.delete")
    public void consumeDelete(MessageDeletedEvent event) {
        messageWebSocketService.messageDeleted(event.messageId(), event.channelId());
    }
}
