package com.flux.flux.v1.userstatus;

import com.flux.flux.v1.userstatus.dto.UserStatusUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatusRepository userStatusRepository;

    public void updateStatus(UserStatusUpdateDTO statusUpdate, String sessionId, String fingerprint) {
        UserStatus status = UserStatus.builder()
                .id(String.format("%d:%s", statusUpdate.userId(), fingerprint))
                .userId(statusUpdate.userId())
                .fingerprint(fingerprint)
                .webSocketSessionId(sessionId)
                .build();

        userStatusRepository.save(status);
    }

    public boolean existsByUserId(Long userId) {
        return userStatusRepository.existsByUserId(userId);
    }
}
