package com.flux.flux.v1.message;

import com.flux.flux.v1.message.dto.MessageDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDTO toDTO(Message message);
}
