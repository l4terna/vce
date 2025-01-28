package com.settings.settings_strife.contollers;

import com.settings.settings_strife.models.User;
import com.settings.settings_strife.repositories.UserRepository;
import com.settings.settings_strife.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(User user, Model model){
        if(!userService.createUser(user)){
            model.addAttribute("errorMessage", "User with email: " + user.getEmail() + " already exists");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/settings/user/{id}")
    public String userSettingsPage(@PathVariable("id") Long id, @RequestParam("email") String email, Model model){

        User user = userRepository.findByEmail(email);

        model.addAttribute("user", user);

        return "user";
    }

    @PostMapping("/settings/user/{id}")
    public String updateUserSettings(@PathVariable("id") Long id, @RequestParam("email") String email,  Model model){

        User user =  userRepository.findByEmail(email);

        user.setUsername(user.getUsername());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setEmail(user.getEmail());

        userRepository.save(user);

        return "redirect:/settings/user" + user.getId();

    }
}
