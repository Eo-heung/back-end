package com.example.backend.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {
    private final UserService userService;
    private final FriendService friendService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/friendList")
    public ResponseEntity<?> getfriendList(@RequestHeader("Authorization") String token) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();

        try {
            // userId를 가지고 옴.
            String toUser = jwtTokenProvider.validateAndGetUsername(token);
            User user = userRepository.findByUserId(toUser).get();
            String toUserNickName = user.getUserName();

            responseDTO.setItems(friendService.getFriends(toUserNickName));
            System.out.println(responseDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/requestFriendList")
    public ResponseEntity<?> getRequestfriendList(@RequestHeader("Authorization") String token) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try {
            // userId를 가지고 옴.
            String toUser = jwtTokenProvider.validateAndGetUsername(token);
            User user = userRepository.findByUserId(toUser).get();
            // 닉네임 가져오기

            List<Map<String, String>> friendsList = new ArrayList<>();

            friendsList = friendService.requestFriends(user.getUserId());

            responseDTO.setItems(friendsList);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/requestFriend")
    public ResponseEntity<?> requestFriend(
//            @RequestHeader("Authorization") String token,
//            String toUser,
//            String fromUser, long req
    ) {
        System.out.println("@@@@@@@@@@@@");
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        /* fromUser먼저*/

        String toUser = "11";
        String fromUser = "1";
        int req = 0;

        try {
//            String toUser = jwtTokenProvider.validateAndGetUsername(token);

            Friend friend = new Friend();

            if(req == 1)
            {
                friend = friendService.requestFriend(fromUser,toUser);
                friend.setStatus(true);

                friend = friendService.saveFriend(friend);
            }
            else {
                friend =  friendService.deleteRequest( fromUser, toUser);
            }



            responseDTO.setItems(friendService.getFriends("11"));
            System.out.println(responseDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}