package com.vce.vce.v1.user;

import com.vce.vce.v1._shared.exception.EntityAlreadyExistsException;
import com.vce.vce.v1.auth.dto.RegisterDTO;
import com.vce.vce.v1.user.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
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
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public List<User> findAllByIds(@NotEmpty List<Long> users) {
        return userRepository.findAllById(users);
    }
}
