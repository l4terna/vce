package com.flux.flux.v1.userpresence;

import com.flux.flux.v1.userpresence.dto.UserPresenceUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserPresenceWebSocketController {
    private final UserPresenceManageService userPresenceManageService;

    @MessageMapping("/presence")
    public void updatePresence (
            UserPresenceUpdateDTO statusUpdate,
            SimpMessageHeaderAccessor headerAccessor) {
        // TODO: ДОПИСАТЬ ЛОГИКУ ДЛЯ СТАТУСОВ "AWAY", "DO NOT DISTURB" и т.д
    }
}
