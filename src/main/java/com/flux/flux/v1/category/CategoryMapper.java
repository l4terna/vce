package com.flux.flux.v1.category;

import com.flux.flux.v1.category.dto.CategoryDTO;
import com.flux.flux.v1.hubs.HubMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {HubMapper.class})
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);
}
