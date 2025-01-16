package com.flux.flux.v1.channel.dto;

import jakarta.validation.constraints.*;

public record CreateHubChannelDTO(
        @NotBlank @Size(max = 255) String name,
        @Positive @NotNull Long categoryId,

        @NotBlank
        @Pattern(regexp = "VOICE|TEXT")
        String type
) {
}
