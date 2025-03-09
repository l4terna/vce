package com.flux.flux.v1.hub.dto;

import com.flux.flux.v1.hub.enumeration.HubType;

public record UpdateHubDTO (
        String name,
        HubType type
){
}
