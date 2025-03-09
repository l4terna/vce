package com.flux.flux.v1.message.event;


public record MessageDeletedEvent(
        Long messageId,
        Long channelId
) {
}
