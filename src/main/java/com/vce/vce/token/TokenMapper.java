package com.vce.vce.token;

import com.vce.vce.token.dto.TokenDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    TokenDTO toDTO(Token token);
}
