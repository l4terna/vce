package com.vce.vce.auth;

import com.vce.vce.auth.dto.AuthDTO;
import com.vce.vce.auth.dto.LoginDTO;
import com.vce.vce.auth.dto.RegisterDTO;
import com.vce.vce.auth.exception.InvalidCredentials;
import com.vce.vce.user.UserMapper;
import com.vce.vce.user.UserService;
import com.vce.vce.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthDTO register(RegisterDTO registerDTO) {
        UserDTO userDTO = userService.createUser(registerDTO);
        String token = jwtService.generateToken(userMapper.toEntity(userDTO));

        return AuthDTO.builder()
                .user(userDTO)
                .token(token)
                .build();
    }

    public AuthDTO login(LoginDTO loginDTO) {
        if (!userService.matchPassword(loginDTO.email(), loginDTO.password())) {
            throw new InvalidCredentials("Incorrect email or password");
        }

        UserDTO userDTO = userService.getByEmail(loginDTO.email());
        String token = jwtService.generateToken(userMapper.toEntity(userDTO));

        return AuthDTO.builder()
                .user(userDTO)
                .token(token)
                .build();
    }
}
