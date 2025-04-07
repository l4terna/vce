package com.flux.flux.v1.message;

import com.flux.flux.v1.media.Media;
import com.flux.flux.v1.message.dto.MessageDTO;
import com.flux.flux.v1.messageread.enumeration.MessageStatus;
import com.flux.flux.v1.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    @Mapping(target = "attachments", expression = "java(getAttachmentKeys(message))")
    MessageDTO toDTO(Message message);

    @Mapping(target = "status", expression = "java(status.getValue())")
    @Mapping(target = "attachments", expression = "java(getAttachmentKeys(message))")
    MessageDTO toDTO(Message message, MessageStatus status);

    @Mapping(target = "readByCount", expression = "java(readByCount)")
    @Mapping(target = "status", expression = "java(status.getValue())")
    @Mapping(target = "attachments", expression = "java(getAttachmentKeys(message))")
    MessageDTO toDTO(Message message, MessageStatus status, long readByCount);

    default List<String> getAttachmentKeys(Message message) {
        return message.getAttachments()
                .stream()
                .map(Media::getStorageKey)
                .toList();
    }
}
