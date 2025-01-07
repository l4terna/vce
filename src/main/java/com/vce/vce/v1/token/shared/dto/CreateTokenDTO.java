package com.vce.vce.v1.token.shared.dto;

import com.vce.vce.v1.user.User;
import com.vce.vce.v1.usersession.UserSession;
import lombok.Builder;

@Builder
public record CreateTokenDTO(
    UserSession userSession,
    User user,
    String token
) {
}
