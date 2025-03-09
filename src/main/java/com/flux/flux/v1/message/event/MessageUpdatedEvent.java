package com.flux.flux.v1.message.event;

import com.flux.flux.v1.message.dto.MessageDTO;

public record MessageUpdatedEvent(
        MessageDTO message,
        Long channelId
) {
}
