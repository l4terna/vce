package com.vce.vce.token.access;

import com.vce.vce.token.shared.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AccessTokenRepository extends TokenRepository<AccessToken> {


}
