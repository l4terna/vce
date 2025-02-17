package com.flux.flux.v1.userpresence.dto;

import java.util.Set;

public record TrackedUsersDTO(
        Set<Long> userIds
) {
}
