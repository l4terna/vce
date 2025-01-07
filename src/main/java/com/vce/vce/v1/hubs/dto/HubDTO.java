package com.vce.vce.v1.hubs.dto;

import com.vce.vce.v1.user.dto.UserDTO;

import java.time.LocalDateTime;

public record HubDTO(
        Long id,
        UserDTO owner,
        String name,
        LocalDateTime createdAt
) {
}
