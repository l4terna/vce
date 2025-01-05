package com.vce.vce.role.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleDTO(
        @NotBlank
        String name,
        @NotBlank
        String color,
        @NotBlank
        String permissions
) {
}
