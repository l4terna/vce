package com.flux.flux.v1.userpresence.tracking;

import com.flux.flux.v1.channel.ChannelService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChannelTrackingService extends AbstractTrackingService {
    private final ChannelService channelService;

    private static final String DG_CHANNEL_ONLINE_MEMBERS = "dg_channel:%d:online_members";

    public ChannelTrackingService(RedisTemplate<String, Object> redisTemplate,
                                  ChannelService channelService,
                                  ApplicationEventPublisher eventPublisher) {
        super(redisTemplate, eventPublisher);
        this.channelService = channelService;
    }

    @Override
    protected String getKeyFormat() {
        return DG_CHANNEL_ONLINE_MEMBERS;
    }

    @Override
    protected Set<Long> getEntityIdsByUserId(Long userId) {
        return channelService.findAllChannelIdsByUserId(userId);
    }

    @Override
    public String getEntityType() {
        return "channel";
    }
}
