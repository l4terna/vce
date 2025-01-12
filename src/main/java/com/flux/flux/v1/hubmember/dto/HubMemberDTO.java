package com.flux.flux.v1.hubmember.dto;

import com.flux.flux.v1.user.dto.UserDTO;

import java.time.LocalDateTime;

public record HubMemberDTO(
        Long id,
        UserDTO user,
        LocalDateTime joinedAt
) {
}
