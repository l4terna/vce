package com.laterna.connexemain.v1.token.shared.dto;

import com.laterna.connexemain.v1.user.User;
import com.laterna.connexemain.v1.usersession.UserSession;
import lombok.Builder;

@Builder
public record CreateTokenDTO(
    UserSession userSession,
    User user,
    String token
) {
}
