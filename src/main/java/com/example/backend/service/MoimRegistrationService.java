package com.example.backend.service;

import com.example.backend.dto.MoimRegistrationDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;


public interface MoimRegistrationService {
    MoimRegistration applyToMoim(int moimId, String userId, MultipartFile moimProfile);

    MoimRegistration cancelMoim(int moimId, String applicantUserId);

    MoimRegistration approveMoim(int moimId, String applicantUserId, String organizerUserId);

    MoimRegistration rejectMoim(int moimId, String applicantUserId, String organizerUserId);

    MoimRegistration quitMoim(int moimId, String applicantUserId);

    MoimRegistration modifyMoimProfile(int moimId, String userId, MultipartFile moimProfile);

    Page<MoimRegistrationDTO> getApplicantList(int moimId, String organizerUserId, String applicantUserNickname, String orderBy, Pageable pageable);

    MoimRegistrationDTO getApplicant(int moimId, int moimRegId, String organizerUserId);

    Map<String, Object> getApplicantByMoimId(int moimId, String loginUser);


    //(모임장)모임 추방
    MoimRegistration kickoutMoim(int moimId, String applicantUserId, String organizerUserId);

    Optional<MoimRegistrationDTO> getMyApplyId(int moimId, String loginUser);

    Page<MoimRegistrationDTO> memberList(int moimId, User user, Pageable pageable);

    boolean verifyMemberRole(User user, Moim moim);
    boolean verifyLeaderRole(User user, Moim moim);

    boolean canAccessMoim(User user, Moim moim);
}