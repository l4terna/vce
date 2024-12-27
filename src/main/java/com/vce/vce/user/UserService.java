package com.vce.vce.user;

import com.vce.vce._shared.exception.EntityAlreadyExistsException;
import com.vce.vce.auth.dto.RegisterDTO;
import com.vce.vce.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public User createUser(RegisterDTO registerDTO) {
        if(userRepository.existsByEmail(registerDTO.email())) {
            throw new EntityAlreadyExistsException("User already exists");
        }

        User user = User.builder()
                .login(registerDTO.login())
                .email(registerDTO.email())
                .password(passwordEncoder.encode(registerDTO.password()))
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public UserDTO create(RegisterDTO registerDTO) {
        return userMapper.toDTO(createUser(registerDTO));
    }

    @Transactional(readOnly = true)
    public boolean matchPassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return passwordEncoder.matches(password, user.getPassword());
    }
}
