package com.flux.flux.v1.message;

import com.flux.flux.v1.message.dto.MessageDTO;
import com.flux.flux.v1.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    MessageDTO toDTO(Message message);

    @Mapping(target = "isRead", expression = "java(readStatus)")
    MessageDTO toDTO(Message message, boolean readStatus);

    @Mapping(target = "readByCount", expression = "java(readByCount)")
    @Mapping(target = "isRead", expression = "java(readStatus)")
    MessageDTO toDTO(Message message, boolean readStatus, long readByCount);
}
