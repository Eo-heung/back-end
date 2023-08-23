package com.example.backend.repository;

import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MoimRepository extends JpaRepository<Moim, Integer> {
    List<Moim> findByUserId(User user);


    Page<Moim> findByMoimTitleContaining(String keyword, Pageable pageable);
    Page<Moim> findByMoimContentContaining(String keyword, Pageable pageable);
    Page<Moim> findByMoimNicknameContaining(String keyword, Pageable pageable);

    Page<Moim> findByMoimCategoryAndMoimTitleContaining(String category, String keyword, Pageable pageable);
    Page<Moim> findByMoimCategoryAndMoimContentContaining(String category, String keyword, Pageable pageable);
    Page<Moim> findByMoimCategoryAndMoimNicknameContaining(String category, String keyword, Pageable pageable);
    Page<Moim> findByMoimCategory(String category, Pageable pageable);
}
