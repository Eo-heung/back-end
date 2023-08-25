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

//    //REJECT 상태 아닌 전체 리스트 호출
//    @Query("SELECT m FROM Moim m WHERE m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
//    Page<Moim> findAllByOrderByMoimIdAsc(User user, Pageable pageable);
//
//    @Query("SELECT m FROM Moim m WHERE m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
//    Page<Moim> findAllByOrderByMoimIdDesc(User user, Pageable pageable);



//////////////////////////////////////////////////////////////////////////////////////
    /////전체 카테고리일 때,
    //전체 서치타입으로 검색
    @Query("SELECT m FROM Moim m WHERE (m.moimTitle LIKE CONCAT('%', :keyword, '%') OR m.moimContent LIKE CONCAT('%', :keyword, '%') OR m.moimNickname LIKE CONCAT('%', :keyword, '%')) AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByAllAsc(User user, String keyword, Pageable pageable);
    @Query("SELECT m FROM Moim m WHERE (m.moimTitle LIKE CONCAT('%', :keyword, '%') OR m.moimContent LIKE CONCAT('%', :keyword, '%') OR m.moimNickname LIKE CONCAT('%', :keyword, '%')) AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByAllDesc(User user, String keyword, Pageable pageable);

    /////전체 카테고리일 때,
    //title 기준으로 검색 후 REJECT 상태가 아닌 리스트 호출
    @Query("SELECT m FROM Moim m WHERE m.moimTitle LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByAllAndMoimTitleAsc(User user, String keyword, Pageable pageable);
    @Query("SELECT m FROM Moim m WHERE m.moimTitle LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByAllAndMoimTitleDesc(User user, String keyword, Pageable pageable);

    //content 기준으로 검색 후 REJECT 상태가 아닌 리스트 호출

    @Query("SELECT m FROM Moim m WHERE m.moimContent LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByAllAndMoimContentAsc(User user, String keyword, Pageable pageable);
    @Query("SELECT m FROM Moim m WHERE m.moimContent LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByAllAndMoimContentDesc(User user, String keyword, Pageable pageable);

    //moimNickname 기준으로 검색 후 REJECT 상태가 아닌 리스트 호출
    @Query("SELECT m FROM Moim m WHERE m.moimNickname LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByAllAndMoimNicknameAsc(User user, String keyword, Pageable pageable);
    @Query("SELECT m FROM Moim m WHERE m.moimNickname LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByAllAndMoimNicknameDesc(User user, String keyword, Pageable pageable);


    /////카테고리 내에서,
    //전체 조건으로 검색
    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND (m.moimTitle LIKE CONCAT('%', :keyword, '%') OR m.moimContent LIKE CONCAT('%', :keyword, '%') OR m.moimNickname LIKE CONCAT('%', :keyword, '%')) AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByMoimCategoryAndAllAsc(User user, String category, String keyword, Pageable pageable);
    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND (m.moimTitle LIKE CONCAT('%', :keyword, '%') OR m.moimContent LIKE CONCAT('%', :keyword, '%') OR m.moimNickname LIKE CONCAT('%', :keyword, '%')) AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByMoimCategoryAndAllDesc(User user, String category, String keyword, Pageable pageable);


    /////카테고리 내에서,
    //title 기준으로 검색 후 REJECT 상태가 아닌 리스트 호출
    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimTitle LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByCategoryAndMoimTitleAsc(User user, String category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimTitle LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByCategoryAndMoimTitleDesc(User user, String category, String keyword, Pageable pageable);

    //content 기준으로 검색 후 REJECT 상태가 아닌 리스트 호출
    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimContent LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByCategoryAndMoimContentAsc(User user, String category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimContent LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByCategoryAndMoimContentDesc(User user, String category, String keyword, Pageable pageable);

    //moimNickname 기준으로 검색 후 REJECT 상태가 아닌 리스트 호출
    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimNickname LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId ASC")
    Page<Moim> findByCategoryAndMoimNicknameAsc(User user, String category, String keyword, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.moimCategory = :category AND m.moimNickname LIKE CONCAT('%', :keyword, '%') AND m.moimId NOT IN (SELECT mr.moim.moimId FROM MoimRegistration mr WHERE mr.user = :user AND mr.regStatus = 'REJECTED') ORDER BY m.moimId DESC")
    Page<Moim> findByCategoryAndMoimNicknameDesc(User user, String category, String keyword, Pageable pageable);







}
