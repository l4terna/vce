package com.vce.vce.usersession;

import com.vce.vce.usersession.dto.UserSessionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSessionMapper {
    UserSessionDTO toDTO(UserSession userSession);

}
