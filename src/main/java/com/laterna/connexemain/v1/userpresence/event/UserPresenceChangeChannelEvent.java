package com.laterna.connexemain.v1.userpresence.event;

import com.laterna.connexemain.v1.userpresence.enumeration.Presence;

public record UserPresenceChangeChannelEvent(
        Long userId,
        Long channelId,
        Presence presence
) {
}
