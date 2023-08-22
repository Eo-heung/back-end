package com.example.backend.controller;

import com.example.backend.dto.MoimRegistrationDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.entity.User;
import com.example.backend.jwt.CustomUserDetails;
import com.example.backend.repository.MoimRegistrationRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.repository.UserRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moimReg")
public class MoimRegistrationController {
    private final MoimRegistrationService moimRegistrationService;
    private final MoimRegistrationRepository moimRegistrationRepository;
    private final UserRepository userRepository;
    private final MoimRepository moimRepository;

    @PostMapping("/apply-moim/{moimId}")
    public ResponseEntity<?> applyToMoim(@PathVariable int moimId,
                                         @AuthenticationPrincipal UserDetails userDetails,
                                         @RequestParam(value = "applicantUserNickname") String applicantUserNickname,
                                         @RequestParam(value = "applicantUserAddr") String applicantUserAddr,
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

    //로그인 사용자의 모임 가입 상태 체크
    @GetMapping("/check-registration-state/{moimId}")
    public ResponseEntity<?> getCurrentUserRegStatus(@PathVariable int moimId,
                                                                              @AuthenticationPrincipal UserDetails userDetails) {
        String currentUser = userDetails.getUsername();

        User currentUserId = userRepository.findByUserId(currentUser)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        Moim currentMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다"));

        List<MoimRegistration> moimRegs = moimRegistrationRepository.findByMoim(currentMoim);

        if (moimRegs.isEmpty()) {
            throw new RuntimeException("모임을 찾을 수 없습니다");
        }

        MoimRegistration moimRegId = moimRegs.get(0);

        MoimRegistration.RegStatus status = moimRegistrationRepository.findByMoimAndUser(currentMoim, currentUserId)
                .map(MoimRegistration::getRegStatus)
                .orElse(null);

        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("moimRegId", moimRegId);

        return ResponseEntity.ok(response);
    }



    @PostMapping("/{moimRegId}/applicant-state")
    public ResponseEntity<?> handleMoim(@PathVariable int moimRegId,
                                        @RequestParam MoimRegistration.RegStatus nowStatus,
                                        @RequestBody(required = false) Map<String, String> body,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        ResponseDTO<String> responseDTO = new ResponseDTO<>();

        String currentUserId = userDetails.getUsername();  //로그인 유저
        String applicantUserId; //신청자
        String organizerUserId; //모임장

        try {
            switch (nowStatus) {
                case CANCELED:
                case QUIT:
                    applicantUserId = currentUserId;
                    if (nowStatus == MoimRegistration.RegStatus.CANCELED) {
                        moimRegistrationService.cancelMoim(moimRegId, applicantUserId);
                    } else {
                        moimRegistrationService.quitMoim(moimRegId, applicantUserId);
                    }
                    break;
                case APPROVED:
                case REJECTED:
                    organizerUserId = currentUserId;

                    if (body == null || !body.containsKey("applicantUserId")) {
                        throw new IllegalArgumentException("request body에서 applicantUserId를 전달받지 못했습니다.");
                    }

                    applicantUserId = body.get("applicantUserId");

                    if (nowStatus == MoimRegistration.RegStatus.APPROVED) {
                        moimRegistrationService.approveMoim(moimRegId, applicantUserId, organizerUserId);
                    } else {
                        moimRegistrationService.rejectMoim(moimRegId, applicantUserId, organizerUserId);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("유효한 모입 가입 신청 내역이 없습니다.");
            }

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

    //신청자 리스트
    @GetMapping("get-applicant-list/{moimId}")
    public ResponseEntity<?> getApplicantList(@PathVariable int moimId,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDTO<Optional<MoimRegistration>> responseDTO = new ResponseDTO<>();

        String organizerUserId = userDetails.getUsername();  //userId

        try {
            List<MoimRegistration> MoimRegList = moimRegistrationService.getApplicantList(moimId, organizerUserId);
            return ResponseEntity.ok().body(MoimRegList);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    //모임 유저 프로필 수정
    @PostMapping("/modify-moim-profile/{moimId}")
    public ResponseEntity<?> modifyMoimProfile(@PathVariable int moimId,
                                               @AuthenticationPrincipal UserDetails userDetails,
                                               @RequestParam(value = "moimProfile") MultipartFile moimProfile) {
        ResponseDTO<MoimRegistrationDTO> responseDTO = new ResponseDTO<>();
        String userId = userDetails.getUsername();

        try {
            MoimRegistration updatedMoimReg = moimRegistrationService.modifyMoimProfile(moimId, userId, moimProfile);
            MoimRegistrationDTO moimRegDTO = updatedMoimReg.EntityToDTO();

            responseDTO.setItem(moimRegDTO);
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            return handleException(e);
        }
    }

    //신청자 상세페이지
    @GetMapping("get-applicant/{moimRegId}")
    public ResponseEntity<?> getApplicant(@PathVariable int moimRegId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDTO<MoimRegistration> responseDTO = new ResponseDTO<>();

        String organizerUserId = userDetails.getUsername();  //userId

        try {
            MoimRegistration moimRegistration = moimRegistrationService.getApplicant(moimRegId, organizerUserId);
            responseDTO.setItem(moimRegistration);

            return ResponseEntity.ok().body(responseDTO);

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
