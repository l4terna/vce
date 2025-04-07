package com.laterna.connexemain.v1.channeltyping.event;

import com.laterna.connexemain.v1.channeltyping.enumeration.ChannelTypingStatus;
import com.laterna.connexemain.v1.user.dto.UserDTO;

public record ChannelTypingStatusChangeEvent(
        UserDTO user,
        Long channelId,
        ChannelTypingStatus channelTypingStatus
) {
}
