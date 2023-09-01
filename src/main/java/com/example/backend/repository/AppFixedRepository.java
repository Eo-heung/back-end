package com.example.backend.repository;

import com.example.backend.entity.AppFixed;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppFixedRepository extends JpaRepository<AppFixed, Integer> {

    @Query("SELECT a FROM AppFixed a WHERE a.appFixedUser = :user AND a.appState = 'CONFIRM' AND " +
            "(a.appBoard.appStart BETWEEN :appStart AND :appEnd OR a.appBoard.appEnd BETWEEN :appStart AND :appEnd)")
    List<AppFixed> findOverlappingAppointments(@Param("user") User user, @Param("appStart") LocalDateTime appStart, @Param("appEnd") LocalDateTime appEnd);

}
