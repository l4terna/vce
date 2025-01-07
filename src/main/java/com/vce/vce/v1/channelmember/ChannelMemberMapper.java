package com.vce.vce.v1.channelmember;

import com.vce.vce.v1.channelmember.dto.ChannelMemberDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelMemberMapper {
    ChannelMemberDTO toDTO(ChannelMember save);
}
