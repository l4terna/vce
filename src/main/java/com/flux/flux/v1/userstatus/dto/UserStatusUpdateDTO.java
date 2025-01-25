package com.flux.flux.v1.userstatus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserStatusUpdateDTO(
        @JsonProperty("userId")
        Long userId
) {
}
