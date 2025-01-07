package com.vce.vce.v1.message.dto;

import java.time.LocalDateTime;

public record MessageDTO(
        Long id,
        String content,
        LocalDateTime createdAt
) {
}
