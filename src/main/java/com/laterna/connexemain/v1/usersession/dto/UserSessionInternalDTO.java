package com.laterna.connexemain.v1.usersession.dto;

import com.laterna.connexemain.v1.user.User;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserSessionInternalDTO(
    Long id,
    User user,
    String deviceInfo,
    String ipAddress,
    String fingerprint,
    Instant lastActivity
) {}