package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User login(String userId, String userPw){
        Optional<User> loginUser = userRepository.findByUserId(userId);

        if(loginUser.isEmpty()) {
            throw new RuntimeException("id not exist");
        }

        if(!passwordEncoder.matches(userPw, loginUser.get().getUserPw())) {
            throw new RuntimeException("wrong pw");
        }

        return loginUser.get();
    }


    public User join(User user) {

        return userRepository.save(user);
    }

    public User newKaKao(String userId)
    {
        if(userRepository.findByUserId(userId).isPresent())
                return userRepository.findByUserId(userId).get();
        else
            return null;
    }

}
