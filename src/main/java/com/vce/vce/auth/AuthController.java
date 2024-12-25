package com.vce.vce.auth;

import com.sun.net.httpserver.HttpServer;
import com.vce.vce.auth.dto.AuthDTO;
import com.vce.vce.auth.dto.LoginDTO;
import com.vce.vce.auth.dto.RegisterDTO;
import jakarta.servlet.http.HttpServletRequest;
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
            @CookieValue String fingerprint,
            @Valid @RequestBody RegisterDTO registerDTO
    ) {
        return ResponseEntity.ok(authService.register(registerDTO, fingerprint));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO> login(
            @CookieValue String fingerprint,
            @Valid @RequestBody LoginDTO loginDTO
    ) {
        return ResponseEntity.ok(authService.login(loginDTO, fingerprint));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue String fingerprint,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization").substring(7);

        authService.logout(token, fingerprint);
        return ResponseEntity.noContent().build();
    }
}
