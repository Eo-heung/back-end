package com.example.backend.controller;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.Hobby;
import com.example.backend.service.HobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hobby")
public class HobbyController {

    private final HobbyService hobbyService;

    @PostMapping("/gethobby")
    public ResponseEntity<?> getHobby() {
        ResponseDTO<Hobby> responseDTO = new ResponseDTO<>();

        try {
            responseDTO.setItems(hobbyService.getHobby());
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
