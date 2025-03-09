package com.flux.flux.v1.userpresence.notification;

import com.flux.flux.v1.userpresence.event.UserPresenceChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPresenceWebSocketNotifier {
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handlePresenceChangeEvent(UserPresenceChangeEvent event) {
        System.out.println(event);
    }
}
