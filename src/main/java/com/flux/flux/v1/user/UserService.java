package com.flux.flux.v1.user;

import com.flux.flux.v1._shared.exception.EntityAlreadyExistsException;
import com.flux.flux.v1.auth.dto.RegisterDTO;
import com.flux.flux.v1.hubmember.HubMemberService;
import com.flux.flux.v1.hubmember.dto.HubMemberDTO;
import com.flux.flux.v1.user.dto.GetUserFilter;
import com.flux.flux.v1.user.dto.UserDTO;
import com.flux.flux.v1.user.dto.UserProfileDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HubMemberService hubMemberService;

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
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
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<User> findAllByIds(List<Long> users) {
        return userRepository.findAllById(users);
    }

    public UserDTO getMe(User user) {
        return userMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public UserProfileDTO getProfile(
            Long id,
            GetUserFilter filter
    ) {
        HubMemberDTO hubMemberDTO = null;

        UserDTO userDTO = userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (filter.getHubId() != null) {
            hubMemberDTO = hubMemberService.findByHubIdAndUserId(filter.getHubId(), userDTO.id());
        }

        return UserProfileDTO.builder()
                .user(userDTO)
                .hubMember(hubMemberDTO)
                .build();
    }

    @Transactional(readOnly = true)
    public Set<Long> findUserIdsByEmails(Set<String> emails) {
        return userRepository.findUserIdsByEmails(emails);
    }
}
