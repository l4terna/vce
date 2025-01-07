package com.vce.vce.v1.usersession;

import com.vce.vce.v1.usersession.dto.UserSessionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSessionMapper {
    UserSessionDTO toDTO(UserSession userSession);

}
