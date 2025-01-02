package com.vce.vce.category;

import com.vce.vce.category.dto.CategoryDTO;
import com.vce.vce.hubs.HubMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {HubMapper.class})
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);
}
