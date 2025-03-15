package com.flux.flux.v1.channeltyping.dto;

import com.flux.flux.v1.channeltyping.enumeration.ChannelTypingStatus;

public record UpdateTypingStatusDTO(
        ChannelTypingStatus typingStatus
) {
}
