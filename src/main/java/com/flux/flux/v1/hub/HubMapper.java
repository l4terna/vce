package com.flux.flux.v1.hub;

import com.flux.flux.v1.hub.dto.HubDTO;
import com.flux.flux.v1.user.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface HubMapper {
    HubDTO toDTO(Hub hub);
}
