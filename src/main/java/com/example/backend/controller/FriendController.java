package com.example.backend.controller;

import com.example.backend.dto.FriendDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.Friend;
import com.example.backend.entity.Hobby;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.FriendRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.FriendService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {
    private final UserService userService;
    private final FriendService friendService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @PostMapping("/friendList")
    public ResponseEntity<?> getFriendList(@RequestHeader("Authorization") String token) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
        try {
            // userId를 가지고 옴.
            String toUser = jwtTokenProvider.validateAndGetUsername(token);
            User user = userRepository.findByUserId(toUser).orElseThrow(() -> new RuntimeException("User not found"));
            List<Map<String, Object>> friendsList = friendService.getFriends(user.getUserId());
            System.out.println(responseDTO);
            List<Map<String, Object>> friendsList1 = friendsList.stream()
                    .map(this::convertProfileToBase64)
                    .collect(Collectors.toList());
            responseDTO.setItems(friendsList1);
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/requestFriendList")
    public ResponseEntity<?> getRequestfriendList(@RequestHeader("Authorization") String token) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
        try {
            // userId를 가지고 옴.
            String toUser = jwtTokenProvider.validateAndGetUsername(token);
            User user = userRepository.findByUserId(toUser).orElseThrow(() -> new RuntimeException("User not found"));

            List<Map<String, Object>> friendsList = friendService.requestFriends(user.getUserId());

            List<Map<String, Object>> friendsList1 = friendsList.stream()
                    .map(this::convertProfileToBase64)
                    .collect(Collectors.toList());

            responseDTO.setItems(friendsList1);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    public Map<String, Object> convertProfileToBase64(Map<String, Object> friendMap) {
        Map<String, Object> newMap = new HashMap<>(friendMap); // 새로운 Map 생성
        byte[] profileData = (byte[]) newMap.get("profile");
        if (profileData != null) {
            String base64Encoded = Base64.getEncoder().encodeToString(profileData);
            newMap.put("profile", base64Encoded);
        }
        return newMap; // 수정된 새로운 Map 반환
    }

    @PostMapping("/requestFriend/{id}")
    public ResponseEntity<?> requestFriend(@PathVariable Long id, @RequestBody FriendDTO friendDTO) {
        Long request = friendDTO.getId();
        ResponseDTO<Friend> responseDTO = new ResponseDTO<>();
        /* fromUser먼저*/

        try {
//            String toUser = jwtTokenProvider.validateAndGetUsername(token);

            Friend friend = new Friend();

            if(request == 1)
            {
                friend = friendRepository.findById(id).get();
                friend.setStatus(true);

                friend = friendService.saveFriend(friend);
            }
            else {
                friendRepository.deleteById(id);
            }

            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setItem(friend);

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}