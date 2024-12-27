package com.vce.vce.token.refresh;

import com.vce.vce.token.shared.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RefreshTokenRepository extends TokenRepository<RefreshToken> {

}
