package com.flux.flux.v1.hubs.dto;

import com.flux.flux.v1.hubs.enumeration.HubType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateHubDTO(
        @NotBlank
        @Size(max = 100)
        String name,

        HubType type
){
}
