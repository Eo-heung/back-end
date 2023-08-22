package com.example.backend.service;

import com.example.backend.entity.MoimRegistration;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface MoimRegistrationService {
    MoimRegistration applyToMoim(int moimId, String userId, MultipartFile moimProfile);

    MoimRegistration cancelMoim(int moimId, String applicantUserId);

    MoimRegistration approveMoim(int moimId, String applicantUserId, String organizerUserId);

    MoimRegistration rejectMoim(int moimId, String applicantUserId, String organizerUserId);

    MoimRegistration quitMoim(int moimId, String applicantUserId);

    MoimRegistration modifyMoimProfile(int moimId, String userId, MultipartFile moimProfile);

    Optional<MoimRegistration> getApplicantList(int moimId, String organizerUserId);
    MoimRegistration getApplicant(int moimRegId, String organizerUserId);

}