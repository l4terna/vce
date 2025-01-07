package com.vce.vce.v1.token.access;

import com.vce.vce.v1.token.shared.TokenMapper;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;

@Mapper(componentModel = "spring")
@Qualifier("accessTokenMapper")
public interface AccessTokenMapper extends TokenMapper<AccessToken> {
}
