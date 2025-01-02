package com.vce.vce.category.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryDTO(
        @NotBlank
        String name,

        @NotBlank
        Integer position
) {
}
