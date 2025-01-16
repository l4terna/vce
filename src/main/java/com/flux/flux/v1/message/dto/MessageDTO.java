package com.flux.flux.v1.message.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageDTO(
        Long id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt
) {
}
