package com.laterna.connexemain.v1.hub;

import com.laterna.connexemain.v1.category.CategoryService;
import com.laterna.connexemain.v1.category.dto.CategoryDTO;
import com.laterna.connexemain.v1.channel.ChannelService;
import com.laterna.connexemain.v1.channel.dto.ChannelDTO;
import com.laterna.connexemain.v1.channel.dto.HubEntitiesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HubEntitiesService {

    private final CategoryService categoryService;
    private final ChannelService channelService;

    @Transactional(readOnly = true)
    public HubEntitiesDTO getHubEntities(Long hubId) {
        List<CategoryDTO> categories = categoryService.getAllCategories(hubId);
        List<ChannelDTO> channels = channelService.getAllChannels(hubId);

        return HubEntitiesDTO.builder()
                .categories(categories)
                .channels(channels)
                .build();
    }
}
