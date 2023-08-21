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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moimReg")
public class MoimRegistrationController {
    private final MoimRegistrationService moimRegistrationService;

    @PostMapping("/apply-moim/{moimId}")
    public ResponseEntity<?> applyToMoim(@PathVariable int moimId,
                                         @AuthenticationPrincipal UserDetails userDetails,
                                         @RequestParam(value = "moimProfile") MultipartFile moimProfile) {
        ResponseDTO<MoimRegistrationDTO> responseDTO = new ResponseDTO<>();

        String applicantUserId = userDetails.getUsername();  //userId

        try {
            MoimRegistration moimReg = moimRegistrationService.applyToMoim(moimId, applicantUserId, moimProfile);
            MoimRegistrationDTO moimRegDTO = moimReg.EntityToDTO();

            responseDTO.setItem(moimRegDTO);

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            return handleException(e);
        }
    }

    //신청자 리스트
    @GetMapping("get-applicant/{moimId}")
    public ResponseEntity<?> getApplicant() {

        return null;
    }

    //신청자 상세페이지
    @GetMapping("get-applicant/{moimRegId}")
    public ResponseEntity<?> getApplicantReg() {

        return null;
    }

    @PostMapping("/cancel-moim/{moimId}")
    public ResponseEntity<?> cancelMoim(@PathVariable int moimId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();

        String applicantUserId = userDetails.getUsername();  //userId

        try {
            moimRegistrationService.cancelMoim(moimId, applicantUserId);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            return handleException(e);
        }
    }


    @PostMapping("/approve-moim/{moimRegId}")
    public ResponseEntity<?> approveMoim(@PathVariable int moimRegId,
                                                    @RequestBody Map<String, String> body,
                                                    @AuthenticationPrincipal UserDetails userDetails) {  // Principal: 로그인한 사용자의 정보
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        System.out.println("여긴 찍히나");
        String applicantUserId = body.get("applicantUserId");
        String organizerUserId = userDetails.getUsername();;  // userId
        System.out.println("모임장" + organizerUserId);
        System.out.println("신청자" + applicantUserId);

        try {
            moimRegistrationService.approveMoim(moimRegId, applicantUserId, organizerUserId);
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


    @PostMapping("/reject-moim/{moimRegId}")
    public ResponseEntity<?> rejectMoim(@PathVariable int moimRegId,
                                                   @RequestBody Map<String, String> body,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();

        String applicantUserId = body.get("applicantUserId");
        String organizerUserId = userDetails.getUsername();;  // userId // userId

        try {
            moimRegistrationService.rejectMoim(moimRegId, applicantUserId, organizerUserId);
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
