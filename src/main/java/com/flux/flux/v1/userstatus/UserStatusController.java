package com.flux.flux.v1.userstatus;

import com.flux.flux.v1.userstatus.dto.UserStatusUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserStatusController {
    private final UserStatusService userStatusService;

    @MessageMapping("/presence")
    @SendTo("/v1/topic/presence")
    public void updatePresence (
            UserStatusUpdateDTO statusUpdate,
            SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String fingerprint = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("__fprid");
        userStatusService.updateStatus(statusUpdate, sessionId, fingerprint);
    }
}
