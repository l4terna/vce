package com.flux.flux.v1.user.dto;

import com.flux.flux.v1.userstatus.enumeration.Status;

import java.time.Instant;

public record UserDTO(
        Long id,
        String login,
        String email,
        Instant createdAt,
        Instant lastActivity,
        Status status
) {
}
