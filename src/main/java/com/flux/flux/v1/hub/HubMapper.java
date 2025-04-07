package com.flux.flux.v1.hub;

import com.flux.flux.v1.hub.dto.HubDTO;
import com.flux.flux.v1.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface HubMapper {
    @Mapping(target = "avatar", expression = "java(hub.getAvatar() == null ? null : hub.getAvatar().getStorageKey())")
    HubDTO toDTO(Hub hub);
}
