package com.vce.vce.auth.dto;

import com.vce.vce.token.shared.dto.TokenDTO;
import com.vce.vce.user.dto.UserDTO;
import lombok.Builder;

@Builder
public record AuthDTO(
        UserDTO user,
        TokenDTO token
) {
}
