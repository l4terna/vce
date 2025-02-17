package com.flux.flux.v1.userpresence.listener;

import com.flux.flux.v1.usersession.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class UserPresenceExpirationListener implements MessageListener {
    private final UserSessionService userSessionService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if (expiredKey.startsWith("user:presence:")) {
            String[] parts = expiredKey.split(":");

            if (parts.length == 5) {
                userSessionService.updateLastActivity(Long.valueOf(parts[2]), parts[4], Instant.now().minusSeconds(30));
            }
        }
    }
}