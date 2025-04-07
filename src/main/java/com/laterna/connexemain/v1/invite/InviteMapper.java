package com.laterna.connexemain.v1.invite;

import com.laterna.connexemain.v1.invite.dto.InviteDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InviteMapper {
    InviteDTO toDTO(Invite invite);
}
