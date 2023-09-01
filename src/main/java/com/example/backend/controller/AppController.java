package com.example.backend.controller;

import com.example.backend.dto.AppBoardDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.AppBoard;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.AppBoardRepository;
import com.example.backend.repository.AppFixedRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppController {
    private final JwtTokenProvider jwtTokenProvider;
    private final AppService appService;
    private final UserRepository userRepository;
    private final AppBoardRepository appBoardRepository;
    private final AppFixedRepository appFixedRepository;

    //약속 만들기
    @PostMapping("/{moimId}/create-app")
    public ResponseEntity<?> createApp(@PathVariable("moimId") int moimId,
                                       @RequestBody AppBoard appBoard,
                                       @RequestHeader("Authorization") String token) {
        ResponseDTO<AppBoardDTO> responseDTO = new ResponseDTO<>();

        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            AppBoard createdAppBoard = appService.createApp(moimId, appBoard, loginUser);
            AppBoardDTO appBoardDTO = createdAppBoard.EntityToDTO(loginUser.getUserName());

            responseDTO.setItem(appBoardDTO);
            responseDTO.setStatusCode(HttpStatus.CREATED.value());

            return ResponseEntity.ok().body(responseDTO);


        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }















}
