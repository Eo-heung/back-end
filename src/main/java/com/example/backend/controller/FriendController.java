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
        ResponseDTO<Friend> responseDTO = new ResponseDTO<>();

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
        ResponseDTO<Friend> responseDTO = new ResponseDTO<>();
        try {
            // userId를 가지고 옴.
            String toUser = jwtTokenProvider.validateAndGetUsername(token);
            User user = userRepository.findByUserId(toUser).get();
            // 닉네임 가져오기.
            String toUserNickName = user.getUserName();

            responseDTO.setItems(friendService.requestFriends(toUserNickName));
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
        ResponseDTO<Friend> responseDTO = new ResponseDTO<>();
        /* fromUser먼저*/

        String toUser = "재민";
        String fromUser = "효준";
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



            responseDTO.setItems(friendService.getFriends("재민"));
            responseDTO.setItem(friend);
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
