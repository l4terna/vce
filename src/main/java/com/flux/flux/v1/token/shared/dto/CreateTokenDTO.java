package com.flux.flux.v1.token.shared.dto;

import com.flux.flux.v1.user.User;
import com.flux.flux.v1.usersession.UserSession;
import lombok.Builder;

@Builder
public record CreateTokenDTO(
    UserSession userSession,
    User user,
    String token
) {
}
