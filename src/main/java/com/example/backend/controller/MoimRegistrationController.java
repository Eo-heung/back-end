package com.example.backend.controller;

import com.example.backend.dto.MoimRegistrationDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.jwt.CustomUserDetails;
import com.example.backend.service.MoimRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moimReg")
public class MoimRegistrationController {
    private final MoimRegistrationService moimRegistrationService;

    @PostMapping("/apply-moim")
    public ResponseEntity<?> applyToMoim(@RequestParam("moimId") int moimId,
                                         @RequestBody Map<String, String> body) {
        ResponseDTO<MoimRegistrationDTO> responseDTO = new ResponseDTO<>();

        String userId = body.get("userId");  //userID로 신청자의 정보 앞단에서 전송 받기

        try {
            MoimRegistration moimReg = moimRegistrationService.applyToMoim(moimId, userId);
            MoimRegistrationDTO moimRegDTO = moimReg.EntityToDTO();

            responseDTO.setItem(moimRegDTO);

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            return handleException(e);
        }
    }


    @PostMapping("/cancel-moim")
    public ResponseEntity<?> cancelMoim(@RequestParam("moimId") int moimId,
                                                   @RequestBody Map<String, String> body) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();

        String userId = body.get("applicantUserId");

        try {
            moimRegistrationService.cancelMoim(moimId, userId);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            return handleException(e);
        }
}


    @PostMapping("/approve-moim")
    public ResponseEntity<?> approveMoim(@RequestParam("moimId") int moimId,
                                                    @RequestBody Map<String, String> body,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {  // Principal: 로그인한 사용자의 정보
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        System.out.println("여긴 찍히나");
        String applicantUserId = body.get("applicantUserId");
        String organizerUserId = customUserDetails.getUsername();  // userId   ///승인이 안된다!!!!!!!!!!
        System.out.println("모임장" + organizerUserId);
        System.out.println("신청자" + applicantUserId);

        try {
            moimRegistrationService.approveMoim(moimId, applicantUserId, organizerUserId);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch (AccessDeniedException ade) { //권한확인
            responseDTO.setStatusCode(HttpStatus.FORBIDDEN.value());
            responseDTO.setErrorMessage(ade.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDTO);
        } catch (Exception e) {
            return handleException(e);
        }
    }


    @PostMapping("/reject-moim")
    public ResponseEntity<?> rejectMoim(@RequestParam("moimId") int moimId,
                                                   @RequestBody Map<String, String> body,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();

        String applicantUserId = body.get("applicantUserId");
        String organizerUserId = customUserDetails.getUsername(); // userId

        try {
            moimRegistrationService.rejectMoim(moimId, applicantUserId, organizerUserId);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (AccessDeniedException ade) {
            responseDTO.setStatusCode(HttpStatus.FORBIDDEN.value());
            responseDTO.setErrorMessage(ade.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDTO);
        } catch (Exception e) {
            return handleException(e);
        }
    }




    private ResponseEntity<?> handleException(Exception e) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();

        if (e instanceof IllegalArgumentException) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);

        } else if (e instanceof EntityNotFoundException) {
            responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);

        } else {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }




}
