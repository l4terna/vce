package com.flux.flux.v1.message.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateMessageDTO(
        @NotBlank
        String content
) {
}
