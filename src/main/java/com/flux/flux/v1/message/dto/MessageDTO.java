package com.flux.flux.v1.message.dto;

import com.flux.flux.v1.user.dto.UserDTO;
import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record MessageDTO(
        Long id,
        String content,
        Instant createdAt,
        Instant lastModifiedAt,
        UserDTO author,
        Long channelId,
        Integer status,
        Long readByCount // only for GROUP_DC, TEXT channels
) {
}
