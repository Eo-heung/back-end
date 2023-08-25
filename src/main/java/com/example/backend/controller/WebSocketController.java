package com.example.backend.controller;


import com.example.backend.dto.UserDTO;
import com.example.backend.entity.Friend;
import com.example.backend.entity.User;
import com.example.backend.repository.FriendRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.FriendService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final FriendService friendService;

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

        List<Map<String, Object>> friendList = friendService.getFriends(user.getUserId());

        for (Map<String, Object> friend : friendList) {
            String friendsId = (String) friend.get("friendsId");

            // friendsId를 사용하여 메시지 전송
            messagingTemplate.convertAndSend("/topic/user-status-updates/" + friendsId,
                    "친구상태변경");
        }
    }
    @Data
    public static class OnlineStatusDto {
        private String status;
    }

    @MessageMapping("/heartbeat/{userId}")
    public void handleHeartbeat(@DestinationVariable String userId) {
        User user = userRepository.findByUserId(userId).orElse(null);

        if (user != null) {
            user.setLastHeartbeat(LocalDateTime.now());
            userRepository.save(user);
        }
    }


}
