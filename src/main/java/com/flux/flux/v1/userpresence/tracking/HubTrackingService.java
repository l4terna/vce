package com.flux.flux.v1.userpresence.tracking;

import com.flux.flux.v1.hub.HubService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class HubTrackingService extends AbstractTrackingService {
    private static final String HUB_ONLINE_MEMBERS = "hub:%d:online_members";
    private final HubService hubService;

    public HubTrackingService(RedisTemplate<String, Object> redisTemplate,
                              HubService hubService,
                              ApplicationEventPublisher eventPublisher) {
        super(redisTemplate, eventPublisher);
        this.hubService = hubService;
    }

    @Override
    protected String getKeyFormat() {
        return HUB_ONLINE_MEMBERS;
    }

    @Override
    protected Set<Long> getEntityIdsByUserId(Long userId) {
        return hubService.findAllHubIdsByUserId(userId);
    }

    @Override
    protected String getEntityType() {
        return "hub";
    }
}
