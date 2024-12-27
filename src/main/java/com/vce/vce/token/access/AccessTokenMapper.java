package com.vce.vce.token.access;

import com.vce.vce.token.shared.TokenMapper;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;

@Mapper(componentModel = "spring")
@Qualifier("accessTokenMapper")
public interface AccessTokenMapper extends TokenMapper<AccessToken> {
}
