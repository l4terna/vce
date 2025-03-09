package com.flux.flux.v1.userpresence.event;

public record UserPresenceChangeEvent(
        Long userId,
        Long entityId,
        String entityType,
        boolean isOnline
) {
}
