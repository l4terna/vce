package com.vce.vce.v1.token.shared.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TokenDTO(
        Long id,
        String token,
        LocalDateTime expiresAt
) {
}
