package com.flux.flux.v1.usersession;

import com.flux.flux.v1.usersession.dto.UserSessionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSessionMapper {
    UserSessionDTO toDTO(UserSession userSession);

}
