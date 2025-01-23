package com.flux.flux.v1.hubs.dto;

import com.flux.flux.v1.hubs.enumeration.HubType;
import com.flux.flux.v1.user.dto.UserDTO;

import java.time.Instant;

public record HubDTO(
        Long id,
        UserDTO owner,
        String name,
        HubType type,
        Instant createdAt
) {
}
