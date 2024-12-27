package com.vce.vce.token.access;

import com.vce.vce.token.shared.TokenMapper;
import com.vce.vce.token.shared.TokenService;
import com.vce.vce.auth.JwtService;
import com.vce.vce.token.shared.dto.CreateTokenDTO;
import com.vce.vce.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccessTokenService extends TokenService<AccessToken> {
    private final JwtService jwtService;

    public AccessTokenService(
            TokenMapper<AccessToken> tokenMapper,
            AccessTokenRepository tokenRepository,
            JwtService jwtService) {
        super(tokenMapper, tokenRepository);
        this.jwtService = jwtService;
    }

    @Override
    protected long getExpirationTime() {
        return jwtService.getJwtAccessExpiration();
    }

    @Override
    protected String generateToken(User user) {
        return jwtService.generateAccessToken(user);
    }

    @Override
    protected AccessToken createTokenEntity(CreateTokenDTO dto, LocalDateTime expiresAt) {
        return AccessToken.builder()
                .token(dto.token())
                .userSession(dto.userSession())
                .expiresAt(expiresAt)
                .build();
    }

    @Override
    public boolean validateToken(String accessToken, String fingerprint) {
        AccessToken token = tokenRepository.findByToken(accessToken)
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));

        return token.getUserSession().getFingerprint().equals(fingerprint)
                && token.getExpiresAt().isAfter(LocalDateTime.now())
                && !token.getIsRevoked();
    }
}
