package com.vce.vce.hubs;

import com.vce.vce.hubs.dto.HubDTO;
import com.vce.vce.user.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface HubMapper {
    HubDTO toDTO(Hub hub);
}
