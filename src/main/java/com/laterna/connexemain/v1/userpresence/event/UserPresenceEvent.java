package com.laterna.connexemain.v1.userpresence.event;

public record UserPresenceEvent(
        Long userId,
        String fingerprint,
        boolean isConnected
) {
}
