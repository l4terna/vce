package com.flux.flux.v1.channelmember;

import com.flux.flux.v1.channelmember.dto.ChannelMemberDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelMemberMapper {
    ChannelMemberDTO toDTO(ChannelMember save);
}
