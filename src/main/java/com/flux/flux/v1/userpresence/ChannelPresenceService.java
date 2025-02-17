package com.flux.flux.v1.userpresence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChannelPresenceService {
    private final RedisTemplate<String, Object> redisTemplate;

    // Ключи для каналов
    private static final String CHANNEL_ONLINE_MEMBERS_KEY = "channel:%d:members:online";
    private static final String CHANNEL_MEMBER_SCORE_KEY = "channel:%d:member:%d:score";

    public void addMemberToChannel(Long channelId, Long userId, double score) {
        String channelKey = String.format(CHANNEL_ONLINE_MEMBERS_KEY, channelId);

        redisTemplate.opsForZSet().add(channelKey, userId.toString(), score);
    }

    public Set<Object> getOnlineMembers(Long channelId, int start, int end) {
        String channelKey = String.format(CHANNEL_ONLINE_MEMBERS_KEY, channelId);

        return redisTemplate.opsForZSet().reverseRange(channelKey, start, end);
    }
}
