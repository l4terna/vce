package com.vce.vce.invite;

import com.vce.vce.invite.dto.InviteDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InviteMapper {
    InviteDTO toDTO(Invite invite);
}
