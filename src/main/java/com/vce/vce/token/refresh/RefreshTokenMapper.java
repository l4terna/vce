package com.vce.vce.token.refresh;

import com.vce.vce.token.shared.TokenMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper extends TokenMapper<RefreshToken> {
}
