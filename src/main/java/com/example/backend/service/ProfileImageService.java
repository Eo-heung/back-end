package com.example.backend.service;

import com.example.backend.entity.ProfileImage;
import com.example.backend.entity.User;
import com.example.backend.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;


    public ProfileImage update(ProfileImage profileImage) {

        //이미지가 이미 있는 경우
        if( getImage(profileImage.getUserId()) != null ){
            ProfileImage image = getImage(profileImage.getUserId());
            image.setFileData(profileImage.getFileData());
            image.setUpdateDatetime(LocalDateTime.now());
            return profileImageRepository.save(image);
        }
        // 이미지가 없는 경우
        profileImage.setCreateDatetime(LocalDateTime.now());
        profileImage.setUpdateDatetime(LocalDateTime.now());
        return profileImageRepository.save(profileImage);
    }

    public ProfileImage getImage(User user) {

        return profileImageRepository.findByUserId(user);
    }

}
