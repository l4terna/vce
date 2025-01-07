package com.vce.vce.v1.usersession.dto;

import com.vce.vce.v1.user.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserSessionInternalDTO(
    Long id,
    User user,
    String deviceInfo,
    String ipAddress,
    String fingerprint,
    LocalDateTime lastActivity
) {}