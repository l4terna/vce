package com.laterna.connexemain.v1.userpresence.dto;

import java.util.Set;

public record TrackedUsersDTO(
        Set<Long> userIds
) {
}
