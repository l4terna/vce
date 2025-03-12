package com.flux.flux.v1.userpresence.event;

import com.flux.flux.v1.userpresence.enumeration.Presence;

public record UserPresenceChangeHubEvent(
        Long userId,
        Long hubId,
        Presence presence
) {
}
