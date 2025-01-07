package com.vce.vce.v1.invite.dto;

import java.time.LocalDateTime;

public record InviteDTO (
        Long id,
        String code,
        Integer maxUses,
        Integer currentUses,
        Boolean isActive,
        LocalDateTime expiresAt
) {
}
