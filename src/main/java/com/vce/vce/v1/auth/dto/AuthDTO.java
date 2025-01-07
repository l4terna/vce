package com.vce.vce.v1.auth.dto;

import com.vce.vce.v1.token.shared.dto.TokenDTO;
import com.vce.vce.v1.user.dto.UserDTO;
import lombok.Builder;

@Builder
public record AuthDTO(
        UserDTO user,
        TokenDTO token
) {
}
