package com.flux.flux.v1.userpresence.tracking;

import com.flux.flux.v1.channel.Channel;
import com.flux.flux.v1.channel.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelTrackingService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ChannelService channelService;

    private static final String GDC_CHANNEL_MEMBERS_KEY = "gdc_channel:%d:online_members";

    public void updateUserPresenceInGroupChannels(Long userId) {
        Set<Channel> userChannels = channelService.findChannelIdsByUserId(userId);

        userChannels.forEach(channel -> {
            String key = String.format(GDC_CHANNEL_MEMBERS_KEY, channel.getId());
            redisTemplate.opsForZSet().add(key, userId.toString(), channel.getOwner().getId().equals(userId) ? 1.0 : 0.0);
        });
    }

    public Set<Long> getAllOnlineGroupChannelUserIds(Long channelId) {
        String key = String.format(GDC_CHANNEL_MEMBERS_KEY, channelId);

        Set<String> userIds = redisTemplate.opsForZSet().range(key, 0, -1);

        if (userIds == null || userIds.isEmpty()) return null;

        return userIds.stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    public void removeUserPresenceFromGroupChannels(Long userId) {
        Set<Channel> userChannels = channelService.findChannelIdsByUserId(userId);

        userChannels.forEach(channel -> {
            String key = String.format(GDC_CHANNEL_MEMBERS_KEY, channel.getId());
            redisTemplate.opsForZSet().remove(key, userId.toString());

            if (Objects.equals(redisTemplate.opsForZSet().size(key), 0L)) {
                redisTemplate.delete(key);
            }
        });
    }
}
