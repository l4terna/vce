package com.vce.vce.token.shared;

import com.vce.vce.token.shared.dto.CreateTokenDTO;
import com.vce.vce.token.shared.dto.TokenDTO;
import com.vce.vce.user.User;
import com.vce.vce.usersession.UserSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public abstract class TokenService<T extends TokenEntity> {
    private final TokenMapper<T> tokenMapper;
    protected final TokenRepository<T> tokenRepository;

    protected TokenService(TokenMapper<T> tokenMapper, TokenRepository<T> tokenRepository) {
        this.tokenMapper = tokenMapper;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public TokenDTO createToken(CreateTokenDTO createDTO) {
        String jwtToken = generateToken(createDTO.user());

        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                Instant.now().plusMillis(getExpirationTime() * 1000),
                ZoneId.systemDefault()
        );

        T token = createTokenEntity(
                CreateTokenDTO.builder()
                        .userSession(createDTO.userSession())
                        .user(createDTO.user())
                        .token(jwtToken)
                        .build(),
                expiresAt
        );

        return tokenMapper.toDTO(tokenRepository.save(token));
    }

    @Transactional
    public void revokeActiveTokens(UserSession userSession) {
        tokenRepository.revokeAllActiveTokensByUserSession(userSession);
    }

    public abstract boolean validateToken(String token, String fingerprint);
    protected abstract long getExpirationTime();
    protected abstract String generateToken(User user);
    protected abstract T createTokenEntity(CreateTokenDTO dto, LocalDateTime expiresAt);
}
