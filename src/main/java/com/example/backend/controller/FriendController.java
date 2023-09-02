package com.example.backend.controller;

import com.example.backend.dto.FriendDTO;
import com.example.backend.dto.PaymentGamDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.ChattingRoomName;
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
import com.example.backend.repository.ChattingRoomNameRepository;
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
    private final PaymentController paymentController;
    private final ChattingRoomNameRepository chattingRoomNameRepository;

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
            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("userId", toUser);

            responseDTO.setItem(returnMap);
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

                // 사용자 ID를 알파벳순으로 정렬
                String fromUser = friend.getFromUser();
                String toUser = friend.getToUser();
                String roomName;

                if (fromUser.compareTo(toUser) > 0) {
                    roomName = toUser + "-" + fromUser;
                } else {
                    roomName = fromUser + "-" + toUser;
                }

                // 고유한 채팅방 이름을 생성합니다.
                ChattingRoomName chattingRoomName = new ChattingRoomName();
                chattingRoomName.setRoomName(roomName);
                friend.setChattingRoomName(chattingRoomName); // 친구에게 채팅방 이름 설정

                friend = friendService.saveFriend(friend); // 연관된 채팅방 이름도 함께 저장됩니다.
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

    @PostMapping("/makefriend/{id}")
    public ResponseEntity<?> makeFriend(@PathVariable String id, @RequestHeader("Authorization") String token) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

        String fromUser = jwtTokenProvider.validateAndGetUsername(token);

        Map<String, Object> returnMap = new HashMap<>();

        try {
            User user = userRepository.findByUserId(fromUser).get();
            // 친구 여부 확인하기
            if (friendRepository.findFriendByFromUserAndToUser(fromUser, id) == null && friendRepository.findFriendByFromUserAndToUser(id, fromUser) == null ){
                // 곶감 수 확인하기
                if(user.getTotalGam() - 5 > 0) {

                    Friend friend = new Friend();

                    friend.setStatus(false);
                    friend.setFromUser(fromUser);
                    friend.setToUser(id);

                    FriendDTO friendDTO = friendRepository.save(friend).EntityToDTO();

                    PaymentGamDTO paymentGamDTO = PaymentGamDTO.builder()
                            .name("친구 요청 비용")
                            .imp_uid("Friend Request Cost")
                            .merchant_uid("5 GotGam")
                            .value(0L)
                            .gotGam((long) -5)
                            .build();

                    paymentController.friendCost(token, paymentGamDTO);

                    returnMap.put("msg", "successRequest");
                } else {
                    returnMap.put("msg", "notEnoughGam"); // 곶감 수 부족할 때
                }
            } else {
                returnMap.put("msg", "alreadyFriend"); // 친구일 때
            }

            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/deleteFriend/{id}")
    public ResponseEntity<?> deleteFriend(@PathVariable Long id) {
        ResponseDTO<Friend> responseDTO = new ResponseDTO<>();
        try {
            // Friend 엔터티를 조회합니다.
            Optional<Friend> optionalFriend = friendRepository.findById(id);

            if (optionalFriend.isPresent()) {
                Friend friend = optionalFriend.get();
                // Friend 엔터티와 연관된 ChattingRoomName 엔터티도 함께 삭제됩니다.
                friendRepository.delete(friend);

                responseDTO.setStatusCode(HttpStatus.OK.value());
                responseDTO.setItem(friend);

                return ResponseEntity.ok().body(responseDTO);
            } else {
                throw new Exception("Friend not found with ID: " + id);
            }
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}