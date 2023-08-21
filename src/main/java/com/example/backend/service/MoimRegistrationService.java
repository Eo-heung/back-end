package com.example.backend.service;

import com.example.backend.entity.MoimRegistration;

public interface MoimRegistrationService {
    MoimRegistration applyToMoim(int moimId, String userId);

    MoimRegistration cancelMoim(int moimId, String applicantUserId);

    MoimRegistration approveMoim(int moimId, String applicantUserId, String organizerUserId);

    MoimRegistration rejectMoim(int moimId, String applicantUserId, String organizerUserId);


}