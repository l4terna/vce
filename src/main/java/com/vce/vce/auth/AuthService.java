package com.vce.vce.auth;

import com.vce.vce.auth.dto.AuthDTO;
import com.vce.vce.auth.dto.LoginDTO;
import com.vce.vce.auth.dto.RegisterDTO;
import com.vce.vce.auth.exception.InvalidCredentials;
import com.vce.vce.token.TokenService;
import com.vce.vce.token.dto.CreateTokenDTO;
import com.vce.vce.token.dto.TokenDTO;
import com.vce.vce.user.UserService;
import com.vce.vce.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;

    public AuthDTO register(RegisterDTO registerDTO, String fingerprint) {
        UserDTO userDTO = userService.createUser(registerDTO);
        TokenDTO tokenDTO = tokenService.createToken(
                CreateTokenDTO.builder()
                        .fingerprint(fingerprint)
                        .user(userDTO)
                        .build()
        );

        return AuthDTO.builder()
                .user(userDTO)
                .token(tokenDTO)
                .build();
    }

    public AuthDTO login(LoginDTO loginDTO, String fingerprint) {
        if (!userService.matchPassword(loginDTO.email(), loginDTO.password())) {
            throw new InvalidCredentials("Incorrect email or password");
        }

        UserDTO userDTO = userService.getByEmail(loginDTO.email());
        TokenDTO tokenDTO = tokenService.createToken(
                CreateTokenDTO.builder()
                        .fingerprint(fingerprint)
                        .user(userDTO)
                        .build()
        );

        return AuthDTO.builder()
                .user(userDTO)
                .token(tokenDTO)
                .build();
    }

    public void logout(String token, String fingerprint) {
        tokenService.revokeToken(token, fingerprint);
        SecurityContextHolder.clearContext();
    }
}
