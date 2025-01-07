package com.vce.vce.v1.user.dto;

public record UserDTO(
        Long id,
        String login,
        String email
) {
}
