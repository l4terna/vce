package com.flux.flux.v1.userpresence.tracking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubTrackingService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String HUB_ONLINE_MEMBERS = "hub:%d:online_members";

    public void addUserToHub(Long userId, Long hubId) {
        redisTemplate.opsForSet().add(String.format(HUB_ONLINE_MEMBERS, hubId), userId);
    }
}
