package com.vce.vce.token.refresh;

import com.vce.vce.auth.JwtService;
import com.vce.vce.token.shared.TokenMapper;
import com.vce.vce.token.shared.TokenService;
import com.vce.vce.token.shared.dto.CreateTokenDTO;
import com.vce.vce.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RefreshTokenService extends TokenService<RefreshToken> {
    private final JwtService jwtService;

    public RefreshTokenService(
            TokenMapper<RefreshToken> tokenMapper,
            RefreshTokenRepository tokenRepository,
            JwtService jwtService) {
        super(tokenMapper, tokenRepository);
        this.jwtService = jwtService;
    }

    @Override
    protected long getExpirationTime() {
        return jwtService.getJwtRefreshExpiration();
    }

    @Override
    protected String generateToken(User user) {
        return jwtService.generateRefreshToken(user);
    }

    @Override
    protected RefreshToken createTokenEntity(CreateTokenDTO dto, LocalDateTime expiresAt) {
        return RefreshToken.builder()
                .userSession(dto.userSession())
                .token(dto.token())
                .expiresAt(expiresAt)
                .build();
    }

    @Override
    public boolean validateToken(String accessToken, String fingerprint) {
        RefreshToken token = tokenRepository.findByToken(accessToken)
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));

        return token.getUserSession().getFingerprint().equals(fingerprint)
                && token.getExpiresAt().isAfter(LocalDateTime.now())
                && !token.getIsRevoked();
    }

    public RefreshToken findByToken(String refreshToken) {
        return tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));
    }
}
