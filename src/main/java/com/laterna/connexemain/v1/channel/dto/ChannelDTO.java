package com.laterna.connexemain.v1.channel.dto;

import com.laterna.connexemain.v1.channel.enumeration.ChannelType;

public record ChannelDTO (
        Long id,
        String name,
        Long categoryId,
        Integer position,
        ChannelType type
) {
}
