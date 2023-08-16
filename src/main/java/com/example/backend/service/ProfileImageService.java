package com.example.backend.service;

import com.example.backend.entity.ProfileImage;
import com.example.backend.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;


    public ProfileImage update(ProfileImage profileImage) {
       return profileImageRepository.save(profileImage);
    }
}
