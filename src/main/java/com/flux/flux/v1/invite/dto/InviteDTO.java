package com.flux.flux.v1.invite.dto;

import java.time.OffsetDateTime;

public record InviteDTO (
        Long id,
        String code,
        Integer maxUses,
        Integer currentUses,
        Boolean isActive,
        OffsetDateTime expiresAt
) {
}
