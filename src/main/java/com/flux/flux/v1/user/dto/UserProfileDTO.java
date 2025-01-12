package com.flux.flux.v1.user.dto;

import com.flux.flux.v1.hubmember.dto.HubMemberDTO;
import lombok.Builder;

@Builder
public record UserProfileDTO(
        UserDTO user,
        HubMemberDTO hubMember
) {
}
