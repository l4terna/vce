package com.flux.flux.v1._shared.websocket.service;

import com.flux.flux.v1.user.User;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class WebSocketCurrentUserService {
    public User getCurrentUser(StompHeaderAccessor headerAccessor) {
        if (headerAccessor.getUser() instanceof UsernamePasswordAuthenticationToken authentication) {
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                if (userDetails instanceof User user) {
                    return user;
                }
            }
        }
        throw new AccessDeniedException("Unauthorized");
    }
}
