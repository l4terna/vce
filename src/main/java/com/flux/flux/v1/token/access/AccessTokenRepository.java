package com.flux.flux.v1.token.access;

import com.flux.flux.v1.token.shared.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AccessTokenRepository extends TokenRepository<AccessToken> {


}
