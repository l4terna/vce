package com.flux.flux.v1.userpresence;

import com.flux.flux.v1.userpresence.enumeration.Presence;
import com.flux.flux.v1.userpresence.tracking.ChannelTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPresenceManageService {
    private final RedisTemplate<String, Object> redisTemplate;
    public static final String USER_DEVICE_PRESENCE_KEY = "user:%d:presence:fingerprint:%s";
    public static final String USER_PRESENCE_KEY = "user:%d:presence";
    private final ChannelTrackingService channelTrackingService;

    public void update(Long userId, String fingerprint) {
        String userDevicePresenceKey = String.format(USER_DEVICE_PRESENCE_KEY, userId, fingerprint);
        String userPresenceKey = String.format(USER_PRESENCE_KEY, userId);

        redisTemplate.opsForValue().set(userDevicePresenceKey, Presence.ONLINE);

        redisTemplate.opsForValue().set(userPresenceKey, fingerprint);

        channelTrackingService.updateUserPresenceInGroupChannels(userId);
    }

    public void delete(Long userId, String fingerprint) {
        String userDevicePresenceKey = String.format(USER_DEVICE_PRESENCE_KEY, userId, fingerprint);
        String userPresenceKey = String.format(USER_PRESENCE_KEY, userId);

        redisTemplate.delete(userDevicePresenceKey);
        redisTemplate.delete(userPresenceKey);

        channelTrackingService.removeUserPresenceFromGroupChannels(userId);
    }
}
