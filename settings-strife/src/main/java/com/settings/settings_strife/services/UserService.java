package com.settings.settings_strife.services;

import com.settings.settings_strife.dto.UserDto;
import com.settings.settings_strife.models.User;
import com.settings.settings_strife.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User save(UserDto userDto){
        User user = new User()
                .setUsername(userDto.getUsername())
                .setEmail(userDto.getEmail())
                .setPhoneNumber(userDto.getNumber())
                .setPassword(userDto.getPassword());


           return userRepository.save(user);
    }


    public boolean createUser(User user){
        String email = user.getEmail();
        if(userRepository.findByEmail(email) != null) return false;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Saving new user with email: {}", email);
        userRepository.save(user);
        return true;
    }

    public List<User> userList(){
        return userRepository.findAll();
    }

}
