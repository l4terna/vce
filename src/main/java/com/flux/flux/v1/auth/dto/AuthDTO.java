package com.flux.flux.v1.auth.dto;

import com.flux.flux.v1.user.dto.UserDTO;
import lombok.Builder;

@Builder
public record AuthDTO(
        UserDTO user,
        String token
) {
}
