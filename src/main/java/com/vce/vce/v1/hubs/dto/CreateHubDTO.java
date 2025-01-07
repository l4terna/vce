package com.vce.vce.v1.hubs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateHubDTO(
        @NotBlank
        @Size(max = 100)
        String name
){
}
