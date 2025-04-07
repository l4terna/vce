package com.laterna.connexemain.v1.usersession.dto;

import com.laterna.connexemain.v1.user.User;
import lombok.Builder;

@Builder
public record CreateUserSessionDTO(
        User user
) {
}
