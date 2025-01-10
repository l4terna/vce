package com.flux.flux.v1.invite;

import com.flux.flux.v1.invite.dto.InviteDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InviteMapper {
    InviteDTO toDTO(Invite invite);
}
