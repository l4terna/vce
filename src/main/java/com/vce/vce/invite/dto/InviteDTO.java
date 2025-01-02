package com.vce.vce.invite.dto;

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
