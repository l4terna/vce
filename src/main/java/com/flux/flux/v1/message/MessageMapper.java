package com.flux.flux.v1.message;

import com.flux.flux.v1.message.dto.MessageDTO;
import com.flux.flux.v1.messagestatus.enumeration.MessageStatus;
import com.flux.flux.v1.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    @Mapping(target = "channelId", source = "message.channel.id")
    MessageDTO toDTO(Message message);

    @Mapping(target = "channelId", source = "message.channel.id")
    @Mapping(target = "status", expression = "java(status.getValue())")
    MessageDTO toDTO(Message message, MessageStatus status);

    @Mapping(target = "readByCount", expression = "java(readByCount)")
    @Mapping(target = "status", expression = "java(status.getValue())")
    @Mapping(target = "channelId", source = "message.channel.id")
    MessageDTO toDTO(Message message, MessageStatus status, long readByCount);
}
