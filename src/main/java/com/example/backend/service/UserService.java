package com.example.backend.service;

import com.example.backend.dto.UserDTO;
import com.example.backend.entity.Friend;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(String userId, String userPw) {
        Optional<User> loginUser = userRepository.findByUserId(userId);

        if (loginUser.isEmpty()) {
            throw new RuntimeException("id not exist");
        }

        if (!passwordEncoder.matches(userPw, loginUser.get().getUserPw())) {
            throw new RuntimeException("wrong pw");
        }

        return loginUser.get();
    }

    public User join(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public boolean userExistsByUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public UserDTO updatePassword(UserDTO userDTO) {
        System.out.println(userDTO);
        // 사용자 ID를 기반으로 사용자를 찾습니다.
        Optional<User> userOptional = userRepository.findByUserId(userDTO.getUserId());

        // 사용자가 존재하는 경우 비밀번호를 업데이트합니다.
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // DTO에서 받은 비밀번호를 암호화합니다.
            String encodedPassword = passwordEncoder.encode(userDTO.getUserPw());

            // 암호화된 비밀번호를 설정합니다.
            user.setUserPw(encodedPassword);

            // 변경된 내용을 저장합니다.
            User updatedUser = userRepository.save(user);

            // 변경된 User 객체를 UserDTO로 변환하여 반환합니다.
            return UserDTO.builder()
                    .userId(updatedUser.getUserId())
                    // 비밀번호를 제외한 다른 필드만 반환합니다.
                    .userName(updatedUser.getUserName())
                    .build();

        } else {
            // 해당 ID를 가진 사용자가 없는 경우 예외를 발생시킵니다.
            throw new RuntimeException("User with the given ID not found");
        }
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
    public User newKaKao(String userId) {
        if (userRepository.findByUserId(userId).isPresent())
            return userRepository.findByUserId(userId).get();
        else
            return null;
    }
    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

}
