package com.vce.vce.invite.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;

public record CreateInviteDTO(
        Integer maxUses,

        @Future
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDateTime expiresAt
) {
}
