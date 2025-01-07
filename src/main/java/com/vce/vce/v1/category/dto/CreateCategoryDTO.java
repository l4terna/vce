package com.vce.vce.v1.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryDTO(
        @NotBlank
        String name
) {
}
