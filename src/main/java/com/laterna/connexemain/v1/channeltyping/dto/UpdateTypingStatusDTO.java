package com.laterna.connexemain.v1.channeltyping.dto;

import com.laterna.connexemain.v1.channeltyping.enumeration.ChannelTypingStatus;

public record UpdateTypingStatusDTO(
        ChannelTypingStatus typingStatus
) {
}
