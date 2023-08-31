package com.example.backend.repository;

import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoimRepository extends JpaRepository<Moim, Integer> {

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



    ////내 모임 오름차순
    @Query(value =
            "SELECT * FROM (" +
                    "   ( " +
                    "   SELECT m.*, m.moim_regdate AS sort_date " +
                    "   FROM MOIM m " +
                    "   WHERE m.user_id = :userId " +
                    "   AND m.moim_title LIKE CONCAT('%', :keyword, '%') " +
                    "   ) " +
                    "   UNION " +
                    "   (" +
                    "   SELECT m.*, mr.subscribe_date AS sort_date " +
                    "   FROM MOIM m JOIN MOIM_REGISTRATION mr ON m.moim_id = mr.moim_id " +
                    "   WHERE mr.user_id = :userId AND mr.reg_status = 'APPROVED' " +
                    "   AND m.moim_title LIKE CONCAT('%', :keyword, '%') " +
                    "   ) " +
                    ") AS results " +
                    "ORDER BY sort_date ASC",
            countQuery = "SELECT COUNT(*) FROM (" +
            "   (SELECT m.moim_id FROM MOIM m WHERE m.user_id = :userId AND m.moim_title LIKE CONCAT('%', :keyword, '%')) " +
            "   UNION " +
            "   (SELECT m.moim_id FROM MOIM m JOIN MOIM_REGISTRATION mr ON m.moim_id = mr.moim_id WHERE mr.user_id = :userId AND mr.reg_status = 'APPROVED' AND m.moim_title LIKE CONCAT('%', :keyword, '%')) " +
            ") AS countResults"
            , nativeQuery = true)
    Page<Moim> findmyMoimAsc(@Param("userId") String userId, @Param("keyword") String keyword, Pageable pageable);

    ////내 모임 내림차순
    @Query(value =
            "SELECT * FROM (" +
                    "   (SELECT m.*, m.moim_regdate AS sort_date " +
                    "   FROM MOIM m " +
                    "   WHERE m.user_id = :userId " +
                    "   AND m.moim_title LIKE CONCAT('%', :keyword, '%') " +
                    "   ) " +
                    "   UNION " +
                    "   (SELECT m.*, mr.subscribe_date AS sort_date " +
                    "   FROM MOIM m JOIN MOIM_REGISTRATION mr ON m.moim_id = mr.moim_id " +
                    "   WHERE mr.user_id = :userId AND mr.reg_status = 'APPROVED' " +
                    "   AND m.moim_title LIKE CONCAT('%', :keyword, '%') " +
                    "   ) " +
                    ") AS results " +
                    "ORDER BY sort_date DESC",
            countQuery = "SELECT COUNT(*) FROM (" +
            "   (SELECT m.moim_id FROM MOIM m WHERE m.user_id = :userId) " +
            "   UNION " +
            "   (SELECT m.moim_id FROM MOIM m JOIN MOIM_REGISTRATION mr ON m.moim_id = mr.moim_id WHERE mr.user_id = :userId AND mr.reg_status = 'APPROVED') " +
            ") AS countResults"
            , nativeQuery = true)
    Page<Moim> findmyMoimDesc(@Param("userId") String userId,  @Param("keyword") String keyword, Pageable pageable);


    List<Moim> findByUserId(User user);
}
