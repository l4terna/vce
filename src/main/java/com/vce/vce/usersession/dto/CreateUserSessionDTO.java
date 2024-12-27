package com.vce.vce.usersession.dto;

import com.vce.vce.user.User;
import lombok.Builder;

@Builder
public record CreateUserSessionDTO(
        User user
) {
}
