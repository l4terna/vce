package com.flux.flux.v1.userpresence.tracking;

import com.flux.flux.v1.userpresence.event.UserPresenceChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractTrackingService {
    protected final RedisTemplate<String, Object> redisTemplate;
    private final ApplicationEventPublisher eventPublisher;
    
    protected abstract String getKeyFormat();
    protected abstract Set<Long> getEntityIdsByUserId(Long userId);
    protected abstract String getEntityType();

    public void updateUserPresence(Long userId) {
        Set<Long> entityIds = getEntityIdsByUserId(userId);
        
        entityIds.forEach(entityId -> {
            String key = String.format(getKeyFormat(), entityId);
            redisTemplate.opsForSet().add(key, userId.toString());

            eventPublisher.publishEvent(new UserPresenceChangeEvent(userId, entityId, getEntityType(), true));
        });
    }
    
    public Set<Long> getAllOnlineUserIds(Long entityId) {
        String key = String.format(getKeyFormat(), entityId);
        Set<Object> userIds = redisTemplate.opsForSet().members(key);
        
        if (userIds == null || userIds.isEmpty()) return null;
        
        return userIds.stream()
                .map(obj -> Long.parseLong(obj.toString()))
                .collect(Collectors.toSet());
    }
    
    public void removeUserPresence(Long userId) {
        Set<Long> entityIds = getEntityIdsByUserId(userId);
        
        entityIds.forEach(entityId -> {
            String key = String.format(getKeyFormat(), entityId);
            redisTemplate.opsForSet().remove(key, userId.toString());
            
            if (Objects.equals(redisTemplate.opsForZSet().size(key), 0L)) {
                redisTemplate.delete(key);
            }

            eventPublisher.publishEvent(new UserPresenceChangeEvent(userId, entityId, getEntityType(), false));
        });
    }

}