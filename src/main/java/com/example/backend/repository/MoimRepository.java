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

    

    Page<Moim> findAllByOrderByMoimIdAsc(Pageable pageable);
    Page<Moim> findAllByOrderByMoimIdDesc(Pageable pageable);

    Page<Moim> findByMoimTitleContainingOrderByMoimIdAsc(String keyword, Pageable pageable);
    Page<Moim> findByMoimTitleContainingOrderByMoimIdDesc(String keyword, Pageable pageable);

    Page<Moim> findByMoimContentContainingOrderByMoimIdAsc(String keyword, Pageable pageable);
    Page<Moim> findByMoimContentContainingOrderByMoimIdDesc(String keyword, Pageable pageable);

    Page<Moim> findByMoimNicknameContainingOrderByMoimIdAsc(String keyword, Pageable pageable);
    Page<Moim> findByMoimNicknameContainingOrderByMoimIdDesc(String keyword, Pageable pageable);

    Page<Moim> findByMoimCategoryAndMoimTitleContainingOrderByMoimIdAsc(String category, String keyword, Pageable pageable);
    Page<Moim> findByMoimCategoryAndMoimTitleContainingOrderByMoimIdDesc(String category, String keyword, Pageable pageable);

    Page<Moim> findByMoimCategoryAndMoimContentContainingOrderByMoimIdAsc(String category, String keyword, Pageable pageable);
    Page<Moim> findByMoimCategoryAndMoimContentContainingOrderByMoimIdDesc(String category, String keyword, Pageable pageable);

    Page<Moim> findByMoimCategoryAndMoimNicknameContainingOrderByMoimIdAsc(String category, String keyword, Pageable pageable);
    Page<Moim> findByMoimCategoryAndMoimNicknameContainingOrderByMoimIdDesc(String category, String keyword, Pageable pageable);

    Page<Moim> findByMoimCategoryOrderByMoimIdAsc(String category, Pageable pageable);

    Page<Moim> findByMoimCategoryOrderByMoimIdDesc(String category, Pageable pageable);
}
