package com.vce.vce.token.access;

import com.vce.vce.token.shared.TokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface AccessTokenRepository extends TokenRepository<AccessToken> {

}
