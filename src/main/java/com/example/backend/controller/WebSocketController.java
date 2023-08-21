package com.example.backend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendNotification/{toUserId}")
    public void sendNotification(@DestinationVariable String toUserId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + toUserId, message);
    }
}
