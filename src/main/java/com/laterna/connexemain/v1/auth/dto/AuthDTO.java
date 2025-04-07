package com.laterna.connexemain.v1.auth.dto;

import com.laterna.connexemain.v1.user.dto.UserDTO;
import lombok.Builder;

@Builder
public record AuthDTO(
        UserDTO user,
        String token
) {
}
