package com.vce.vce.hubs.dto;

import com.vce.vce.user.dto.UserDTO;

import java.time.LocalDateTime;

public record HubDTO(
        Long id,
        UserDTO owner,
        String name,
        LocalDateTime createdAt
) {
}
