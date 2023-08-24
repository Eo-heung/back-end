package com.example.backend.repository;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MoimRegistrationRepository extends JpaRepository<MoimRegistration, Integer> {
    Optional<MoimRegistration> findByMoimAndUser(Moim moim, User user);

    @Query("SELECT mr FROM MoimRegistration mr INNER JOIN mr.moim m WHERE m.userId.userId = :userId AND mr.regStatus = 'WAITING' AND m.moimId = :moimId  order by m.moimId asc")
    Page<MoimRegistration> findApplicantsByMoimIdAndUserIdOrderByMoimIdAsc(@Param("moimId") int moimId, @Param("userId") String userId, Pageable pageable);

    @Query("SELECT mo FROM MoimRegistration mo INNER JOIN mo.moim m WHERE m.userId.userId = :userId AND mo.regStatus = 'WAITING' AND m.moimId = :moimId order by m.moimId desc")
    Page<MoimRegistration> findApplicantsByMoimIdAndUserIdOrderByMoimIdDesc(@Param("moimId") int moimId, @Param("userId") String userId, Pageable pageable);


}


