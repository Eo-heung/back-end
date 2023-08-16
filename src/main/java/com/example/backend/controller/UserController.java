package com.example.backend.controller;

import com.example.backend.api.GeoLocation;
import com.example.backend.api.SmsService;
import com.example.backend.dto.GeoLocationResponse;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final SmsService smsService;

    private final GeoLocation geoLocation;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserDTO userDTO) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();
        System.out.println(userDTO);
        try {
            User user = userDTO.DTOToEntity();

            user.setUserPw(
                    passwordEncoder.encode(userDTO.getUserPw())
            );

            //회원가입처리(화면에서 보내준 내용을 디비에 저장)
            User joinUser = userService.join(user);
            joinUser.setUserPw("");

            UserDTO joinUserDTO = joinUser.EntityToDTO();

            responseDTO.setItem(joinUserDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        try {
            //아이디가 존재하면 해당 아이디에 대한 유저정보가 담김
            //아이디가 존재하지 않으면 null이 담김
            User user = userService.login(userDTO.getUserId(), userDTO.getUserPw());

            if(user != null) {
                String token = jwtTokenProvider.create(user);
                user.setUserPw("");

                UserDTO loginUserDTO = user.EntityToDTO();
                loginUserDTO.setToken(token);

                responseDTO.setItem(loginUserDTO);
                responseDTO.setStatusCode(HttpStatus.OK.value());

                return ResponseEntity.ok().body(responseDTO);
            } else {
                responseDTO.setErrorMessage("login failed");
                return ResponseEntity.badRequest().body(responseDTO);
            }
        } catch(Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> autoLogin(@RequestBody String token) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        System.out.println(token);
        try {
            String userName = jwtTokenProvider.validateAndGetUsername(token);
            System.out.println(userName);
            responseDTO.setItem(userName);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/checkphone")
    public ResponseEntity<?> checkPhone(@RequestBody String tel){
        Random rand = new Random();
        ResponseDTO responseDTO = new ResponseDTO();
        String phoneNum = tel.substring(0, 11);

        String numStr = "";
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }
        System.out.println("회원가입 문자 인증 => " + numStr);

        try {
            System.out.println(phoneNum);
            smsService.sendMsg(phoneNum, numStr);
            responseDTO.setItem(numStr);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            System.out.println(responseDTO);
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @GetMapping("/getlocation")
    public ResponseEntity<?> getLocation(@RequestParam String ip) {
        ResponseDTO<GeoLocationResponse> responseDTO = new ResponseDTO();

        try {
            System.out.println(ip);
            responseDTO.setItem(geoLocation.getGeoLocation(ip));
            responseDTO.setStatusCode(HttpStatus.OK.value());
            System.out.println(responseDTO);
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO userDTO) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();

        try {
            if (userService.userExistsByUserId(userDTO.getUserId())) {
                userService.updatePassword(userDTO); // UserDTO 객체를 전달합니다.
                responseDTO.setStatusCode(HttpStatus.OK.value());
                responseDTO.setItem("Password Updated");
            } else {
                responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
                responseDTO.setItem("User Not Found");
            }

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @PostMapping("/idcheck")
    public ResponseEntity<?> findPW(@RequestBody UserDTO userDTO) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        try {
            // userTel을 userId로 사용하기 때문에, userTel 값을 가져와서 확인
            String userTel = userDTO.getUserId();

            if (userService.userExistsByUserId(userTel)) {
                // 비번 재설정 페이지로 이동하는 로직
                responseDTO.setStatusCode(HttpStatus.OK.value());
                responseDTO.setItem("vaildId");
            } else {
                // alert 창 띄우고 navi.signUp 페이지로 이동하는 로직
                responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
                responseDTO.setItem("InvaildId");
            }
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @PostMapping("/getUserInfo")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserInfo(@RequestHeader("Authorization") String token) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        try {
            String userId = jwtTokenProvider.validateAndGetUsername(token);
            System.out.println(jwtTokenProvider.validateAndGetUsername(token));
            UserDTO userDTO = userService.getUserInfo(userId);

            if (userDTO != null) {
                responseDTO.setItem(userDTO);
                responseDTO.setStatusCode(HttpStatus.OK.value());
                System.out.println(responseDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            } else {
                responseDTO.setErrorMessage("User not Found");
                responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
                System.out.println(responseDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            System.out.println(responseDTO);
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


}