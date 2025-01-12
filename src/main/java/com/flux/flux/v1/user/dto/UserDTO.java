package com.flux.flux.v1.user.dto;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String login,
        String email,
        LocalDateTime createdAt
) {
}
