package com.example.backend.repository;

import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MoimRepository extends JpaRepository<Moim, Integer> {
    List<Moim> findByUserId(User user);



    @Query("SELECT m FROM Moim m WHERE m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findAllByOrderByMoimIdAsc(User user, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findAllByOrderByMoimIdDesc(User user, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimTitle LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByMoimTitleContainingOrderByMoimIdAsc(User user, int category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimTitle LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByMoimTitleContainingOrderByMoimIdDesc(User user, int category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimContent LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByMoimContentContainingOrderByMoimIdAsc(User user, int category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimContent LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByMoimContentContainingOrderByMoimIdDesc(User user, int category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimNickname LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByMoimNicknameContainingOrderByMoimIdAsc(User user, int category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimNickname LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByMoimNicknameContainingOrderByMoimIdDesc(User user, int category, String keyword, Pageable pageable);


//    dyddyydydydydydrl

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimTitle LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByMoimCategoryAndMoimTitleContainingOrderByMoimIdAsc(User user, int category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimTitle LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByMoimCategoryAndMoimTitleContainingOrderByMoimIdDesc(User user, int category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimContent LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByMoimCategoryAndMoimContentContainingOrderByMoimIdAsc(User user, int category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimContent LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByMoimCategoryAndMoimContentContainingOrderByMoimIdDesc(User user, int category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimNickname LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByMoimCategoryAndMoimNicknameContainingOrderByMoimIdAsc(User user, int category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimNickname LIKE %:keyword% AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByMoimCategoryAndMoimNicknameContainingOrderByMoimIdDesc(User user, int category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByMoimCategoryOrderByMoimIdAsc(User user, int category, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByMoimCategoryOrderByMoimIdDesc(User user, int category, Pageable pageable);
}
