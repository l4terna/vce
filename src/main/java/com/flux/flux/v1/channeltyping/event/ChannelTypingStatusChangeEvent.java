package com.flux.flux.v1.channeltyping.event;

import com.flux.flux.v1.channeltyping.enumeration.ChannelTypingStatus;
import com.flux.flux.v1.user.dto.UserDTO;

public record ChannelTypingStatusChangeEvent(
        UserDTO user,
        Long channelId,
        ChannelTypingStatus channelTypingStatus
) {
}
