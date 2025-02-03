package com.flux.flux.v1.message.event;

import com.flux.flux.v1.message.dto.MessageDTO;
import lombok.Builder;

@Builder
public record MessageUpdatedEvent(
        MessageDTO message,
        Long channelId
) implements MessageEvent {
}
