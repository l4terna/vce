package com.flux.flux.v1.message.event;

import com.flux.flux.v1.message.dto.MessageDTO;


public record MessageCreatedEvent(
        MessageDTO message,
        Long channelId
) {
}
