package com.flux.flux.v1.userstatus;

import com.flux.flux.v1.userstatus.dto.UserStatusUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserStatusController {
    private final UserStatusService userStatusService;
    private final UserStatusDeletionService userStatusDeletionService;

    @MessageMapping("/presence")
    @SendTo("/topic/presence")
    public void updatePresence (
            UserStatusUpdateDTO statusUpdate,
            SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String fingerprint = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("__fprid");
        userStatusService.updateStatus(statusUpdate, sessionId, fingerprint);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        userStatusDeletionService.delete(sessionId);
    }
}
