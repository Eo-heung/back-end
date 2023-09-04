package com.example.backend.repository;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface MoimRegistrationRepository extends JpaRepository<MoimRegistration, Integer> {
    Optional<MoimRegistration> findByMoimAndUser(Moim moim, User user);


    @Query("select mr from MoimRegistration mr where mr.moim.moimId = :moimId and mr.user.userId = :userId")
    Optional<MoimRegistration> findByMoimAndUser2(@Param("moimId") int moimId, @Param("userId")String userId);

    @Query("SELECT mr FROM MoimRegistration mr INNER JOIN mr.moim m WHERE m.userId.userId = :userId AND mr.regStatus = 'WAITING' AND m.moimId = :moimId  order by m.moimId asc")
    Page<MoimRegistration> findApplicantsByMoimIdAndUserIdOrderByMoimIdAsc(@Param("moimId") int moimId, @Param("userId") String userId, Pageable pageable);

    @Query("SELECT mo FROM MoimRegistration mo INNER JOIN mo.moim m WHERE m.userId.userId = :userId AND mo.regStatus = 'WAITING' AND m.moimId = :moimId order by m.moimId desc")
    Page<MoimRegistration> findApplicantsByMoimIdAndUserIdOrderByMoimIdDesc(@Param("moimId") int moimId, @Param("userId") String userId, Pageable pageable);


    Optional<MoimRegistration> findByMoim(Moim checkMoim);


    Optional<MoimRegistration> findByUserAndMoim_MoimId(User user, int moimId);

    List<MoimRegistration> findAllByMoim(Moim moim);

    MoimRegistration findByMoim_MoimIdAndUser_UserId(int moimId, String userId);

    @Query("SELECT m FROM MoimRegistration m WHERE m.moim.moimId = :moimId " +
            "AND (m.regStatus = 'LEADER'OR m.regStatus = 'APPROVED') " +
            " ORDER BY CASE WHEN m.regStatus = 'LEADER' THEN 1 ELSE 2 END, m.subscribeDate DESC")
    Page<MoimRegistration> findAllMember(@Param("moimId") int moimId, Pageable pageable);







}



