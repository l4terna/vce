package com.flux.flux.v1.usersession.dto;

import com.flux.flux.v1.user.User;
import lombok.Builder;

@Builder
public record CreateUserSessionDTO(
        User user
) {
}
