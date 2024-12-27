package com.vce.vce.usersession.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserSessionDTO(
        Long id,
        String deviceInfo,
        String ipAddress,
        LocalDateTime lastActivity,
        Boolean isActive
) {
}
