package com.vce.vce.token.shared.dto;

import com.vce.vce.user.User;
import com.vce.vce.usersession.UserSession;
import lombok.Builder;

@Builder
public record CreateTokenDTO(
    UserSession userSession,
    User user,
    String token
) {
   public CreateTokenDTO {}
}
