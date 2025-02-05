package com.flux.flux.v1.message;

import com.flux.flux.v1.message.dto.MessageDTO;
import com.flux.flux.v1.messageread.enumeration.MessageStatus;
import com.flux.flux.v1.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    MessageDTO toDTO(Message message);

    @Mapping(target = "status", expression = "java(status.getValue())")
    MessageDTO toDTO(Message message, MessageStatus status);

    @Mapping(target = "readByCount", expression = "java(readByCount)")
    @Mapping(target = "status", expression = "java(status.getValue())")
    MessageDTO toDTO(Message message, MessageStatus status, long readByCount);
}
