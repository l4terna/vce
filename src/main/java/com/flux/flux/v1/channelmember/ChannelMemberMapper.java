package com.flux.flux.v1.channelmember;

import com.flux.flux.v1.channel.ChannelMapper;
import com.flux.flux.v1.channelmember.dto.ChannelMemberDTO;
import com.flux.flux.v1.user.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ChannelMapper.class})
public interface ChannelMemberMapper {
    ChannelMemberDTO toDTO(ChannelMember save);
}
