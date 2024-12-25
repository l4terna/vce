package com.vce.vce.token;

import com.vce.vce.auth.JwtService;
import com.vce.vce.token.dto.CreateTokenDTO;
import com.vce.vce.token.dto.TokenDTO;
import com.vce.vce.token.enumeration.TokenType;
import com.vce.vce.user.User;
import com.vce.vce.user.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;

    @Transactional
    public TokenDTO createToken(CreateTokenDTO createDTO) {
        User user = userMapper.toEntity(createDTO.user());

        String jwt = jwtService.generateToken(user);

        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                Instant.now().plusMillis(jwtService.getJwtExpiration() * 1000), // converting to millis
                ZoneId.systemDefault()
        );

        revokeUserTokensByFingerprint(user, createDTO.fingerprint());

        Token token = Token.builder()
                .accessToken(jwt)
                .user(user)
                .type(TokenType.BEARER)
                .expiresAt(expiresAt)
                .fingerprint(createDTO.fingerprint())
                .build();

        return tokenMapper.toDTO(tokenRepository.save(token));
    }

    @Transactional
    public void revokeUserTokensByFingerprint(User user, String fingerprint) {
        List<Token> tokens = tokenRepository.findAllActiveTokens(user, fingerprint)
                .stream()
                .peek(token -> token.setIsRevoked(true))
                .toList();

        tokenRepository.saveAll(tokens);
    }

    @Transactional(readOnly = true)
    public boolean validateToken(String accessToken, String fingerprint) {
        Token token = tokenRepository.findByAccessTokenAndFingerprint(accessToken, fingerprint)
                .orElseThrow(() -> new EntityNotFoundException("Access token not found"));

        return !token.getIsRevoked() && token.getExpiresAt().isAfter(LocalDateTime.now());
    }

    @Transactional
    public void revokeToken(String accessToken, String fingerprint) {
        Token token = tokenRepository.findByAccessTokenAndFingerprint(accessToken, fingerprint)
                .orElseThrow(() -> new EntityNotFoundException("Access token not found"));

        token.setIsRevoked(true);
        tokenRepository.save(token);
    }
}
