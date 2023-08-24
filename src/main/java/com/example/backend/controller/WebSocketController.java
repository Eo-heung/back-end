package com.example.backend.controller;


import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    @MessageMapping("/sendNotification/{toUserId}")
    public void sendNotification(@DestinationVariable String toUserId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + toUserId, message);
    }
    @MessageMapping("/online-status/{userId}")
    public void handleOnlineStatus(@DestinationVariable String userId, OnlineStatusDto onlineStatusDto) {
        // 여기에서 userId와 status를 처리합니다.
        // 예: 온라인 상태를 데이터베이스에 저장하거나 다른 사용자에게 알림

        String status = onlineStatusDto.getStatus();

        System.out.println("status : " + status);
        User user = userRepository.findByUserId(userId).get();

        if(status.equals("online")) {
            user.setOnline(true);
        } else {
            user.setOnline(false);
        }

        userRepository.save(user);

        // 필요한 경우, 메시지를 다른 주제로 전송할 수 있습니다.
        // messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
    }

    @Data
    public static class OnlineStatusDto {
        private String status;
    }

}
