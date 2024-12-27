package com.vce.vce.token.shared.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TokenDTO(
        Long id,
        String token,
        LocalDateTime expiresAt,
        Boolean isRevoked,
        LocalDateTime createdAt
) {
}
