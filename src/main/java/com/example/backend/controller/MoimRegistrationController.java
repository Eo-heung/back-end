package com.example.backend.controller;

import com.example.backend.dto.MoimDTO;
import com.example.backend.dto.MoimRegistrationDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimPicture;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.MoimRegistrationRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.BoardService;
import com.example.backend.service.MoimRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moimReg")
public class MoimRegistrationController {
    private final MoimRegistrationService moimRegistrationService;
    private final BoardService boardService;
    private final MoimRegistrationRepository moimRegistrationRepository;
    private final UserRepository userRepository;
    private final MoimRepository moimRepository;
    private final JwtTokenProvider jwtTokenProvider;

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
    public ResponseEntity<Map<String, Object>> getCurrentUserRegStatus(@PathVariable int moimId, @AuthenticationPrincipal UserDetails userDetails) {
        String currentUser = userDetails.getUsername();

        User currentUserId = userRepository.findByUserId(currentUser)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        Moim currentMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다"));

        MoimRegistration registration = moimRegistrationRepository.findByMoimAndUser(currentMoim, currentUserId)
                .orElse(null); // 여기서 해당 조건에 맞는 MoimRegistration을 찾습니다.


        if (registration == null ) {
            throw new RuntimeException("등록 정보를 찾을 수 없습니다");

        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", registration.getRegStatus());
        response.put("moimRegId", registration.getMoimRegId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{moimRegId}/applicant-state")
    public ResponseEntity<?> handleMoim(@PathVariable int moimRegId,
                                        @RequestParam("nowStatus") MoimRegistration.RegStatus nowStatus,
                                        @RequestBody(required = false) Map<String, String> body,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        ResponseDTO<String> responseDTO = new ResponseDTO<>();

        String currentUserId = userDetails.getUsername();  //로그인 유저
        User currentUser = userRepository.findById(currentUserId).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        Optional<MoimRegistration> registrationOpt = moimRegistrationRepository.findById(moimRegId);
        if (!registrationOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Moim registration not found.");
        }

        int currentMoimId = registrationOpt.get().getMoim().getMoimId();

        String applicantUserId; //신청자
        String organizerUserId; //모임장

        try {
            switch (nowStatus) {
                case CANCELED:
                case QUIT:
                    applicantUserId = currentUserId;
                    if (nowStatus == MoimRegistration.RegStatus.CANCELED) {
                        moimRegistrationService.cancelMoim(currentMoimId, applicantUserId);
                    } else {
                        moimRegistrationService.quitMoim(currentMoimId, applicantUserId);
                    }
                    break;
                case APPROVED:
                case REJECTED:
                    if (body == null || !body.containsKey("applicantUserId")) {
                        throw new IllegalArgumentException("request body에서 applicantUserId를 전달받지 못했습니다.");
                    }

                    applicantUserId = body.get("applicantUserId");
                    organizerUserId = body.get("organizerUserId");

                    if (nowStatus == MoimRegistration.RegStatus.APPROVED) {
                        moimRegistrationService.approveMoim(currentMoimId, applicantUserId, organizerUserId);
                    } else {
                        moimRegistrationService.rejectMoim(currentMoimId, applicantUserId, organizerUserId);
                    }
                    break;
                case KICKOUT:
                    if (body == null || !body.containsKey("applicantUserId")) {
                        throw new IllegalArgumentException("request body에서 applicantUserId를 전달받지 못했습니다.");
                    }

                    applicantUserId = body.get("applicantUserId");
                    organizerUserId = body.get("organizerUserId");

                    if (nowStatus == MoimRegistration.RegStatus.APPROVED) {
                        moimRegistrationService.approveMoim(currentMoimId, applicantUserId, organizerUserId);
                    } else {
                        moimRegistrationService.kickoutMoim(currentMoimId, applicantUserId, organizerUserId);
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
    @PostMapping("/get-applicant-list/asc/{moimId}")
    public ResponseEntity<?> getApplicantListAsc(@PathVariable int moimId,
                                              @RequestParam(required = false, defaultValue = "") String applicantUserNickname,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam("orderBy")String orderBy,
                                             @RequestHeader("Authorization") String token) {
        ResponseDTO<MoimRegistrationDTO> responseDTO = new ResponseDTO<>();

        String userId = jwtTokenProvider.validateAndGetUsername(token);

        try {
            Pageable pageable = PageRequest.of(0, (page + 1) * 3);

            Page<MoimRegistrationDTO> moimRegDTOPage = moimRegistrationService.getApplicantList(moimId, userId, applicantUserNickname, "ascending", pageable);

            responseDTO.setItems(moimRegDTOPage.getContent());
            responseDTO.setLastPage(moimRegDTOPage.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping("/get-applicant-list/desc/{moimId}")
    public ResponseEntity<?> getApplicantListDesc(@PathVariable int moimId,
                                              @RequestParam(required = false, defaultValue = "") String applicantUserNickname,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam("orderBy")String orderBy,
                                              @RequestHeader("Authorization") String token) {
        ResponseDTO<MoimRegistrationDTO> responseDTO = new ResponseDTO<>();

        String userId = jwtTokenProvider.validateAndGetUsername(token);


        try {
            Pageable pageable = PageRequest.of(0, (page + 1) * 3);

            Page<MoimRegistrationDTO> moimRegDTOPage = moimRegistrationService.getApplicantList(moimId, userId, applicantUserNickname, "descending", pageable);

            responseDTO.setItems(moimRegDTOPage.getContent());
            responseDTO.setLastPage(moimRegDTOPage.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    //신청자 상세페이지
    @GetMapping("/get-applicant/{moimId}/{moimRegId}")
    public ResponseEntity<?> getApplicant(@PathVariable int moimId,
                                          @PathVariable int moimRegId,
                                          @RequestHeader("Authorization") String token) {
        ResponseDTO<MoimRegistrationDTO> responseDTO = new ResponseDTO<>();

        String userId = jwtTokenProvider.validateAndGetUsername(token);

        try {
            MoimRegistrationDTO moimRegistrationDTO = moimRegistrationService.getApplicant(moimId, moimRegId, userId);
            responseDTO.setItem(moimRegistrationDTO);

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            return handleException(e);
        }
    }

    //모임인원 불러오기
    @GetMapping("/member-list/{moimId}")
    public ResponseEntity<?> memberList(@PathVariable int moimId,
                                        Pageable pageable,
                                        @RequestHeader("Authorization") String token,
                                        @RequestParam(defaultValue = "0") int currentPage) {
        ResponseDTO<Page<MoimRegistrationDTO>> responseDTO = new ResponseDTO<>();

        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        pageable = PageRequest.of(currentPage, 3);

        try {
            Page<MoimRegistrationDTO> result = moimRegistrationService.memberList(moimId, loginUser, pageable);

            ResponseDTO.PaginationInfo paginationInfo = new ResponseDTO.PaginationInfo();
            paginationInfo.setTotalPages(result.getTotalPages());
            paginationInfo.setCurrentPage(result.getNumber());
            paginationInfo.setTotalElements(result.getTotalElements());

            responseDTO.setItem(result);
            responseDTO.setPaginationInfo(paginationInfo);
            responseDTO.setLastPage(result.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
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

    //모임 유저 프로필 확인
    @GetMapping("/view-moim-profile/{moimId}")
    public ResponseEntity<?> viewMoimProfile(@PathVariable int moimId,
                                             @RequestHeader("Authorization") String token) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
        String loginUserId = jwtTokenProvider.validateAndGetUsername(token);
        Map<String, Object> returnMap = new HashMap<>();

        try {
            // moimId에 해당하는 신청자의 상세 내용 가져오기
            Map<String, Object> checkMap = moimRegistrationService.getApplicantByMoimId(moimId, loginUserId);

            if(checkMap.get("msg") == null) {
                returnMap.put("msg", "okValue");
                returnMap.put("applicantDetails", checkMap.get("moimRegistration"));
            } else {
                returnMap.put("msg", checkMap.get("msg"));
            }

            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("get-my-apply/{moimId}")
    public ResponseEntity<?> getMyApplicant(@PathVariable int moimId,
                                            @RequestHeader("Authorization") String token) {
        try {
            // 토큰에서 사용자 정보 가져오기
            String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
            User loginUser = userRepository.findByUserId(loggedInUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 해당 사용자가 moimId에 신청을 했는지 확인
            Optional<MoimRegistrationDTO> moimRegDTO = moimRegistrationService.getMyApplyId(moimId, loggedInUsername);
            if (!moimRegDTO.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No application found for the given moimId");
            }

            // 데이터를 클라이언트에게 응답
            return ResponseEntity.ok(moimRegDTO.get());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
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
