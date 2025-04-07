package com.laterna.connexemain.v1.userpresence.event;

import com.laterna.connexemain.v1.userpresence.enumeration.Presence;

public record UserPresenceChangeHubEvent(
        Long userId,
        Long hubId,
        Presence presence
) {
}
