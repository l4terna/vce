package com.vce.vce.v1.channelmember.dto;

import com.vce.vce.v1.channel.dto.ChannelDTO;
import com.vce.vce.v1.user.dto.UserDTO;

import java.time.LocalDateTime;

public record ChannelMemberDTO(
        Long id,
        ChannelDTO channel,
        UserDTO user,
        LocalDateTime joinedAt
) {
}
