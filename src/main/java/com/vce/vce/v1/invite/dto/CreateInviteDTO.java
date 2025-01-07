package com.vce.vce.v1.invite.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateInviteDTO(
        @Positive
        Integer maxUses,

        @Future
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDateTime expiresAt
) {
}
