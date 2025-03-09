package com.flux.flux.v1.userpresence.event;

public record UserPresenceEvent(
        Long userId,
        String fingerprint,
        boolean isConnected
) {
}
