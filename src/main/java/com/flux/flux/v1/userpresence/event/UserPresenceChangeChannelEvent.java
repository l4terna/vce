package com.flux.flux.v1.userpresence.event;

import com.flux.flux.v1.userpresence.enumeration.Presence;

public record UserPresenceChangeChannelEvent(
        Long userId,
        Long channelId,
        Presence presence
) {
}
