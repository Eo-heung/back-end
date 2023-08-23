package com.example.backend.repository;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MoimRegistrationRepository extends JpaRepository<MoimRegistration, Integer> {
    Optional<MoimRegistration> findByMoimAndUser(Moim moim, User user);

    Optional<MoimRegistration> findByUser(User user);
    List<MoimRegistration> findByMoim(Moim moim);


    long countByMoimAndRegStatus(Moim moim, MoimRegistration.RegStatus regStatus);

    @Query("SELECT m FROM MoimRegistration m JOIN m.user u WHERE m.moim = :moim AND m.regStatus = 'WAITING' AND u.userNickname LIKE %:moimNickname%")
    Page<MoimRegistration> findByMoimAndUserNickname(Moim moim, String moimNickname, Pageable pageable);
}

