package com.vce.vce.token.dto;

import com.vce.vce.user.dto.UserDTO;
import lombok.Builder;

@Builder
public record CreateTokenDTO(
        UserDTO user,
        String fingerprint
) {
}
