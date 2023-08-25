package com.example.backend.service;

import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
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

    public UserDTO getUserInfo(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get().EntityToDTO();
        } else {
            return null;
        }
    }

    @Scheduled(fixedRate = 10 * 60 * 1000) // 10분마다 온라인 상태 친구 확인 로직
    public void checkUserHeartbeats() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);

        List<User> users = userRepository.findAllByOnlineTrueAndLastHeartbeatBefore(threshold);
        for (User user : users) {
            user.setOnline(false);
            userRepository.save(user);
        }
    }

}
