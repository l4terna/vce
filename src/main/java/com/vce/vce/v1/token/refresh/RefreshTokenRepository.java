package com.vce.vce.v1.token.refresh;

import com.vce.vce.v1.token.shared.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RefreshTokenRepository extends TokenRepository<RefreshToken> {

}
