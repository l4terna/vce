package com.vce.vce.auth;

import com.vce.vce._shared.security.jwt.JwtService;
import com.vce.vce.auth.dto.AuthDTO;
import com.vce.vce.auth.dto.LoginDTO;
import com.vce.vce.auth.dto.RegisterDTO;
import com.vce.vce.token.access.AccessTokenService;
import com.vce.vce.token.refresh.RefreshToken;
import com.vce.vce.token.refresh.RefreshTokenService;
import com.vce.vce.token.shared.dto.CreateTokenDTO;
import com.vce.vce.token.shared.dto.TokenDTO;
import com.vce.vce.user.User;
import com.vce.vce.user.UserMapper;
import com.vce.vce.user.UserService;
import com.vce.vce.usersession.UserSession;
import com.vce.vce.usersession.UserSessionService;
import com.vce.vce.usersession.dto.CreateUserSessionDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccessTokenService accessTokenService;
    private final UserSessionService userSessionService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthDTO register(RegisterDTO registerDTO, HttpServletResponse response) {
        saveFingerprintCookie(response, registerDTO.fingerprint());
        User user = userService.createUser(registerDTO);
        TokenDTO accessTokenDTO = createAuthenticationSession(user, registerDTO.fingerprint(), response);

        return AuthDTO.builder()
                .user(userMapper.toDTO(user))
                .token(accessTokenDTO)
                .build();
    }

    @Transactional
    public AuthDTO login(LoginDTO loginDTO, HttpServletResponse response) {
        if (!userService.matchPassword(loginDTO.email(), loginDTO.password())) {
            throw new AccessDeniedException("Invalid email or password");
        }

        saveFingerprintCookie(response, loginDTO.fingerprint());
        User user = userService.findByEmail(loginDTO.email());
        userSessionService.deactivatePreviousSessions(user, loginDTO.fingerprint());
        TokenDTO accessTokenDTO = createAuthenticationSession(user, loginDTO.fingerprint(), response);

        return AuthDTO.builder()
                .user(userMapper.toDTO(user))
                .token(accessTokenDTO)
                .build();
    }

    private void saveFingerprintCookie(HttpServletResponse response, String fingerprint) {
        ResponseCookie fingerprintCookie = ResponseCookie.from("__fprid", fingerprint)
                .httpOnly(true)
                .secure(false)
//                .partitioned(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(jwtService.getJwtRefreshExpiration())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, fingerprintCookie.toString());
    }

    private TokenDTO createAuthenticationSession(User user, String fingerprint, HttpServletResponse response) {
        CreateUserSessionDTO createUserSessionDTO = CreateUserSessionDTO.builder()
                .user(user)
                .build();
        UserSession userSession = userSessionService.createUserSession(createUserSessionDTO, fingerprint);

        CreateTokenDTO createTokenDTO = CreateTokenDTO.builder()
                .userSession(userSession)
                .user(user)
                .build();

        accessTokenService.revokeActiveTokens(userSession);

        TokenDTO accessTokenDTO = accessTokenService.createToken(createTokenDTO);
        TokenDTO refreshTokenDTO = refreshTokenService.createToken(createTokenDTO);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("__rtid", refreshTokenDTO.token())
                .httpOnly(true)
                .secure(false)
//                .partitioned(true)
                .path("/api/auth")
                .sameSite("Lax")
                .maxAge(jwtService.getJwtRefreshExpiration())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return accessTokenDTO;
    }

    @Transactional
    public void logout(String refreshToken, String fingerprint, HttpServletResponse response) {
        RefreshToken refreshTokenEntity = refreshTokenService.findByToken(refreshToken);

        if (!refreshTokenService.validateToken(refreshToken, fingerprint)) {
            userSessionService.deactivateSessionCompletely(refreshTokenEntity.getUserSession());

            throw new AccessDeniedException("Invalid refresh token");
        }

        userSessionService.deactivateSessionCompletely(refreshTokenEntity.getUserSession());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("__rtid", "")
                .maxAge(0)
                .secure(false)
//                .partitioned(true)
                .path("/api/auth")
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    @Transactional
    public AuthDTO refreshToken(String refreshToken, String fingerprint) {
        RefreshToken refreshTokenEntity = refreshTokenService.findByToken(refreshToken);

        if (!refreshTokenService.validateToken(refreshToken, fingerprint)) {
            userSessionService.deactivateSessionCompletely(refreshTokenEntity.getUserSession());

            throw new AccessDeniedException("Invalid refresh token");
        }

        accessTokenService.revokeActiveTokens(refreshTokenEntity.getUserSession());

        CreateTokenDTO createTokenDTO = CreateTokenDTO.builder()
                .userSession(refreshTokenEntity.getUserSession())
                .user(refreshTokenEntity.getUserSession().getUser())
                .build();

        TokenDTO accessTokenDTO = accessTokenService.createToken(createTokenDTO);

        return AuthDTO.builder()
                .user(userMapper.toDTO(refreshTokenEntity.getUserSession().getUser()))
                .token(accessTokenDTO)
                .build();
    }
}
