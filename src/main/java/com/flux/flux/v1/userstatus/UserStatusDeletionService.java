package com.flux.flux.v1.userstatus;

import com.flux.flux.v1.usersession.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserStatusDeletionService {
    private final UserStatusRepository userStatusRepository;
    private final UserSessionService userSessionService;

    public void delete(String sessionId) {
        userStatusRepository.findByWebSocketSessionId(sessionId)
                .ifPresent(status -> {
                    userSessionService.updateLastActivity(status.getUserId(), status.getFingerprint(), Instant.now());
                    userStatusRepository.delete(status);
                });
    }
}
