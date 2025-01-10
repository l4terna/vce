package com.flux.flux.v1.token.refresh;

import com.flux.flux.v1.token.shared.TokenMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper extends TokenMapper<RefreshToken> {
}
