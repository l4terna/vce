package com.flux.flux.v1.channel.dto;

import com.flux.flux.v1.channel.enumeration.ChannelType;
import jakarta.validation.constraints.*;

public record CreateHubChannelDTO(
        @NotBlank @Size(max = 255) String name,
        @Positive @NotNull Long categoryId,
        @NotNull ChannelType type
) {
}
