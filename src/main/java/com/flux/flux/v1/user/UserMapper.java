package com.flux.flux.v1.user;

import com.flux.flux.v1.user.dto.UserDTO;
import com.flux.flux.v1.usersession.UserSessionService;
import com.flux.flux.v1.userstatus.UserStatusService;
import com.flux.flux.v1.userstatus.enumeration.Status;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    private UserStatusService userStatusService;

    @Autowired
    private UserSessionService userSessionService;

    @Mapping(target = "status", expression = "java(getStatus(user))")
    @Mapping(target = "lastActivity", expression = "java(getLastActivity(user))")
    public abstract UserDTO toDTO(User user);

    Status getStatus(User user) {
        if (userStatusService.existsByUserId(user.getId())) {
            return Status.ONLINE;
        }
        return Status.OFFLINE;
    }

    Instant getLastActivity(User user) {
        if (userStatusService.existsByUserId(user.getId())) {
            return Instant.now();
        }

        return userSessionService.getLastActivity(user.getId());
    }
}