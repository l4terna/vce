package com.laterna.connexemain.v1.hubmember;

import com.laterna.connexemain.v1.hubmember.dto.HubMemberDTO;
import com.laterna.connexemain.v1.user.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface HubMemberMapper {
    HubMemberDTO toDTO(HubMember hubMember);
}
