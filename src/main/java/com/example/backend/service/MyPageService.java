package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User editInfo(User user) {
        return userRepository.save(user);
    }

    public String checkPw(String userId, String userPw){
        Optional<User> loginUser = userRepository.findByUserId(userId);

        if(!passwordEncoder.matches(userPw, loginUser.get().getUserPw())) {
            return "invalidPassword";
        } else {
            return "validPassword";
        }
    }

}
