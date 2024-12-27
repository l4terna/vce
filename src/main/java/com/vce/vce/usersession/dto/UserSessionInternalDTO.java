package com.vce.vce.usersession.dto;

import com.vce.vce.user.User;
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