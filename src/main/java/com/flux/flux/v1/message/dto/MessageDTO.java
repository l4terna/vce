package com.flux.flux.v1.message.dto;

import com.flux.flux.v1.user.dto.UserDTO;
import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageDTO(
        Long id,
        String content,
        Instant createdAt,
        UserDTO author,
        Boolean isRead, // only for DC channels
        Long readByCount // only for GROUP_DC, TEXT channels
) {
}
