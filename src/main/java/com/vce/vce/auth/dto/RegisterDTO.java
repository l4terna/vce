package com.vce.vce.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank
        @Size(min = 3, max = 50)
        String login,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 6)
        String password
) {
}
