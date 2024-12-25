package com.vce.vce.token.dto;

import java.time.LocalDateTime;

public record TokenDTO(
        String accessToken,
        LocalDateTime expiresAt,
        Boolean isRevoked
) {
}
