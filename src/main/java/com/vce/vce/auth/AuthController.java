package com.vce.vce.auth;

import com.vce.vce.auth.dto.AuthDTO;
import com.vce.vce.auth.dto.LoginDTO;
import com.vce.vce.auth.dto.RegisterDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthDTO> register(
            @Valid @RequestBody RegisterDTO registerDTO,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.register(registerDTO, response));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO> login(
            @Valid @RequestBody LoginDTO loginDTO,
            @CookieValue String fingerprint,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.login(loginDTO, fingerprint, response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthDTO> refresh(
            @CookieValue(name = "refresh_token") String refreshToken,
            @CookieValue String fingerprint
    ) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken, fingerprint));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refresh_token") String refreshToken,
            @CookieValue String fingerprint,
            HttpServletResponse response
    ) {
        authService.logout(refreshToken, fingerprint, response);
        return ResponseEntity.noContent().build();
    }
}
