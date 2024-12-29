package com.vce.vce.hubs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrUpdateHubDTO(
        @NotBlank
        @Size(min = 1, max = 100)
        String name
){
}
