package com.flux.flux.v1.role;

import com.flux.flux.v1.role.dto.RoleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toDTO(Role role);
}
