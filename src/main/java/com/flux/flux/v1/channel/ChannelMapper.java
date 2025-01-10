package com.flux.flux.v1.channel;

import com.flux.flux.v1.channel.dto.ChannelDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    ChannelDTO toDTO(Channel channel);
}
