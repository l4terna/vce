package com.flux.flux.v1.hubmember;

import com.flux.flux.v1.hubmember.dto.HubMemberDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HubMemberMapper {
    HubMemberDTO toDTO(HubMember hubMember);
}
