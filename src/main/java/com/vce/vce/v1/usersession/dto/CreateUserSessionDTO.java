package com.vce.vce.v1.usersession.dto;

import com.vce.vce.v1.user.User;
import lombok.Builder;

@Builder
public record CreateUserSessionDTO(
        User user
) {
}
