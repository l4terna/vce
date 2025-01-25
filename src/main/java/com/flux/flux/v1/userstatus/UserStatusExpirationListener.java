package com.flux.flux.v1.userstatus;

import com.flux.flux.v1.usersession.UserSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserStatusExpirationListener implements MessageListener {
    private final UserSessionService userSessionService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if (expiredKey.startsWith("user_status:")) {
            String[] parts = expiredKey.split(":");
            userSessionService.updateLastActivity(Long.valueOf(parts[1]), parts[2], Instant.now().minusSeconds(30));
        }
    }
}