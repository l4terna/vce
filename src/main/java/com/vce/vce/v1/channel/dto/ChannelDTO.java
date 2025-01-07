package com.vce.vce.v1.channel.dto;

public record ChannelDTO (
        Long id,
        String name,
        Integer position,
        String type
) {
}
