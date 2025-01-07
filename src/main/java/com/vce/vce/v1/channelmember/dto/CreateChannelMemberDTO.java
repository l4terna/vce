package com.vce.vce.v1.channelmember.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateChannelMemberDTO(
        @NotEmpty
        List<Long> users
) {
}
