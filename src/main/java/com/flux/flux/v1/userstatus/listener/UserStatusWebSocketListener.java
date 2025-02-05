package com.flux.flux.v1.userstatus.listener;

import com.flux.flux.v1.userstatus.UserStatusDeletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class UserStatusWebSocketListener {
    private final UserStatusDeletionService userStatusDeletionService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        userStatusDeletionService.delete(sessionId);
    }
}
