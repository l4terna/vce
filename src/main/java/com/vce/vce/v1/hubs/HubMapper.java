package com.vce.vce.v1.hubs;

import com.vce.vce.v1.hubs.dto.HubDTO;
import com.vce.vce.v1.user.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface HubMapper {
    HubDTO toDTO(Hub hub);
}
