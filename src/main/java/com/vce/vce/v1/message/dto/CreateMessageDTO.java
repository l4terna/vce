package com.vce.vce.v1.message.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateMessageDTO(
        @NotBlank
        String content
) {
}
