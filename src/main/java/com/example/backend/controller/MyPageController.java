package com.example.backend.controller;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.MoimRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.MyPageService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MoimRepository moimRepository;

    private final MyPageService myPageService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/myinfo")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserInfo(@RequestHeader("Authorization") String token) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        try {
            String userId = jwtTokenProvider.validateAndGetUsername(token);
            UserDTO userDTO = userService.getUserInfo(userId);

            if (userDTO != null) {
                responseDTO.setItem(userDTO);
                responseDTO.setStatusCode(HttpStatus.OK.value());
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            } else {
                responseDTO.setErrorMessage("User not Found");
                responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/mymoim")
    public ResponseEntity<?> getUserMoim(@RequestHeader("Authorization") String token) {
        ResponseDTO<Moim> responseDTO = new ResponseDTO<>();

        try {
            String userId = jwtTokenProvider.validateAndGetUsername(token);
            User user = userRepository.findByUserId(userId).get();

            List<Moim> moimList = moimRepository.findByUserId(user);

            responseDTO.setItems(moimList);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @PostMapping("/editinfo")
    public ResponseEntity<?> editInfo(@RequestHeader("Authorization") String token, @RequestBody UserDTO userDTO) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        try {
            String userId = jwtTokenProvider.validateAndGetUsername(token);
            User user = userRepository.findByUserId(userId).get();

            user.setUserName(userDTO.getUserName());
            user.setUserAddr1(userDTO.getUserAddr1());
            user.setUserAddr2(userDTO.getUserAddr2());
            user.setUserAddr3(userDTO.getUserAddr3());
            user.setUserHobby1(userDTO.getUserHobby1());
            user.setUserHobby2(userDTO.getUserHobby2());
            user.setUserHobby3(userDTO.getUserHobby3());
            user.setUserRecommend(userDTO.getUserRecommend());
            user.setUserStatusMessage(userDTO.getUserStatusMessage());
            user.setUserUpdate(LocalDateTime.now());


            //회원정보수정(화면에서 보내준 내용을 디비에 저장)
            User editUser = myPageService.editInfo(user);
            UserDTO editUserDTO = editUser.EntityToDTO();
            editUserDTO.setUserPw("");

            responseDTO.setItem(editUserDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/changephone")
    public ResponseEntity<?> changeNum(@RequestHeader("Authorization") String token, @RequestBody UserDTO userDTO) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        try {
            String userId = jwtTokenProvider.validateAndGetUsername(token);
            User user = userRepository.findByUserId(userId).get();

            System.out.println(userDTO);

            user.setUserTel(userDTO.getUserTel());
            user.setUserEmail(userDTO.getUserEmail());
            user.setUserUpdate(LocalDateTime.now());


            //전화번호수정(화면에서 보내준 내용을 디비에 저장)
            User editUser = myPageService.editInfo(user);
            UserDTO editUserDTO = editUser.EntityToDTO();
            editUserDTO.setUserPw("");

            responseDTO.setItem(editUserDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/checkpassword")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token, @RequestBody UserDTO userDTO) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();

        try {
            String userId = jwtTokenProvider.validateAndGetUsername(token);
            User user = userRepository.findByUserId(userId).get();

            System.out.println(userDTO);

            String result = myPageService.checkPw(userId, userDTO.getUserPw());

            responseDTO.setItem(result);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> changePw(@RequestHeader("Authorization") String token, @RequestBody UserDTO userDTO) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        try {
            String userId = jwtTokenProvider.validateAndGetUsername(token);
            User user = userRepository.findByUserId(userId).get();

            System.out.println(userDTO);

            user.setUserPw( passwordEncoder.encode(userDTO.getUserPw()));
            user.setUserUpdate(LocalDateTime.now());


            //전화번호수정(화면에서 보내준 내용을 디비에 저장)
            User editUser = myPageService.editInfo(user);
            UserDTO editUserDTO = editUser.EntityToDTO();
            editUserDTO.setUserPw("");

            responseDTO.setItem(editUserDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


}
