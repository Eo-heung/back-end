package com.example.backend.controller;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.SirenDTO;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.service.SirenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import com.example.backend.service.SirenService;
import com.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/siren")
public class SirenController {

    private final SirenService sirenService;  // final 키워드 사용
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/report/{id}")
    public ResponseEntity<?> createSirenReport(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestPart(required = false, value = "singoImgA") MultipartFile singoImg1,
            @RequestPart(required = false, value = "singoImgB") MultipartFile singoImg2,
            @RequestPart(required = false, value = "singoImgC") MultipartFile singoImg3,
            @RequestPart("sirenDTO") String sirenData) throws IOException {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();



        try {
            SirenDTO sirenDTO = new ObjectMapper().readValue(sirenData, SirenDTO.class);
            String singoUser = jwtTokenProvider.validateAndGetUsername(token);
            sirenDTO.setSingozaId(singoUser);
            sirenDTO.setPisingozaId(String.valueOf(id));

            if (singoImg1 != null && !singoImg1.isEmpty()) {
                byte[] fileBytes1 = singoImg1.getBytes();
                sirenDTO.setSingoImg1(fileBytes1);
            }

            if (singoImg2 != null && !singoImg2.isEmpty()) {
                byte[] fileBytes2 = singoImg2.getBytes();
                sirenDTO.setSingoImg2(fileBytes2);
            }

            if (singoImg3 != null && !singoImg3.isEmpty()) {
                byte[] fileBytes3 = singoImg3.getBytes();
                sirenDTO.setSingoImg3(fileBytes3);
            }

            sirenService.createSirenReport(sirenDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
//        return ResponseEntity.ok("singo successfully.");

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @PostMapping("/ban")
    public ResponseEntity<?> ban(@RequestBody String userId, @RequestBody Integer date) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();
        try {
            User user = userService.findUserByUserId(userId).get();
            LocalDateTime banDate = LocalDateTime.now().plusDays(date);
            user.setBan(banDate);

            userService.join(user);

            responseDTO.setStatusCode(HttpStatus.OK.value());
            return null;
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}

