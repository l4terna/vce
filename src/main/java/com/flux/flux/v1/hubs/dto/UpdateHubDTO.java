package com.flux.flux.v1.hubs.dto;

import com.flux.flux.v1.hubs.enumeration.HubType;

public record UpdateHubDTO (
        String name,
        HubType type
){
}
