package com.example.backend.controller;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
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

}
