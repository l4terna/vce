package com.laterna.connexemain.v1.user;

import com.laterna.connexemain.v1.user.dto.UserDTO;
import com.laterna.connexemain.v1.userpresence.UserPresenceService;
import com.laterna.connexemain.v1.usersession.UserSessionService;
import com.laterna.connexemain.v1.userpresence.enumeration.Presence;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    protected UserPresenceService userPresenceService;

    @Autowired
    protected UserSessionService userSessionService;

    @Mapping(target = "presence", expression = "java(userPresenceService.getUserPresence(user.getId()))")
    @Mapping(target = "lastActivity", expression = "java(getLastActivity(user.getId()))")
    public abstract UserDTO toDTO(User user);


    Instant getLastActivity(Long userId) {
        if (userPresenceService.getUserPresence(userId) == Presence.ONLINE) {
            return null;
        }

        return userSessionService.getLastActivity(userId);
    }
}