package com.flux.flux.v1.channel.dto;

import com.flux.flux.v1.category.dto.CategoryDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record HubEntitiesDTO (
        List<CategoryDTO> categories,
        List<ChannelDTO> channels
) {
}
