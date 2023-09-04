package com.example.backend.repository;

import com.example.backend.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserId(String userId);


    boolean existsByUserId(String userId);


    List<User> findAllByOnlineTrueAndLastHeartbeatBefore(LocalDateTime threshold);

}
