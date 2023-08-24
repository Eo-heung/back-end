package com.example.backend.repository;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MoimRegistrationRepository extends JpaRepository<MoimRegistration, Integer> {
    Optional<MoimRegistration> findByMoimAndUser(Moim moim, User user);

    Optional<MoimRegistration> findByUser(User user);
    List<MoimRegistration> findByMoim(Moim moim);


    long countByMoimAndRegStatus(Moim moim, MoimRegistration.RegStatus regStatus);

//    @Query("SELECT m FROM MoimRegistration m JOIN m.user u ON u.userId = :userId WHERE m.moim = :moim AND m.regStatus = 'WAITING' AND u.userNickname LIKE %:applicantUserNickname%")
//    Page<MoimRegistration> findByMoimAndUserNickname(Moim moim, String userId, String applicantUserNickname, Pageable pageable);

    @Query("SELECT m FROM MoimRegistration m JOIN m.user u ON u.userId = :userId WHERE m.moim.moimId = :moim AND m.regStatus = 'WAITING' AND u.userNickname LIKE %:applicantUserNickname%")
    List<MoimRegistration> findByMoimAndUserNickname(int moim, String userId, String applicantUserNickname);

    @Query("SELECT m FROM MoimRegistration m JOIN m.user u ON u.userId = :userId WHERE m.moim = :moim AND m.regStatus = 'WAITING'")

    List<MoimRegistration> findByMoimId(Moim moim, String userId);

    @Query("SELECT mr FROM MoimRegistration mr INNER JOIN mr.moim m WHERE m.userId.userId = :userId AND mr.regStatus = 'WAITING' AND m.moimId = :moimId")
    Page<MoimRegistration> findApplicantsByMoimIdAndUserId(@Param("moimId") int moimId, @Param("userId") String userId, Pageable pageable);


}

