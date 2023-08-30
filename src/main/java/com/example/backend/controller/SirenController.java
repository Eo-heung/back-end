package com.example.backend.controller;

import com.example.backend.dto.SirenDTO;
import com.example.backend.service.SirenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/siren")
public class SirenController {

    private final SirenService sirenService;  // final 키워드 사용

    @PostMapping("/report")
    public ResponseEntity<String> createSirenReport(
            @RequestParam(required = false,value = "singoImgA") MultipartFile singoImg1,
            @RequestParam(required = false,value = "singoImgB") MultipartFile singoImg2,
            @RequestParam(required = false,value = "singoImgC") MultipartFile singoImg3,
            SirenDTO sirenDTO) throws IOException {
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
        return ResponseEntity.ok("singo successfully.");
    }


}

