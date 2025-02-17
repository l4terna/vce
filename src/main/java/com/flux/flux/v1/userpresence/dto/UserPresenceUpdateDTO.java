package com.flux.flux.v1.userpresence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserPresenceUpdateDTO(
        @JsonProperty("userId")
        Long userId
) {
}
