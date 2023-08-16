package com.example.backend.repository;

import com.example.backend.entity.ProfileImage;
import com.example.backend.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
public interface ProfileImageRepository extends JpaRepository<ProfileImage, String> {

}
