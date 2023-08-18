package com.example.backend.repository;


import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoimRepository extends JpaRepository<Moim, Integer> {
    Optional<Moim> findByUserId(User user);

    Page<Moim> findByMoimTitleContainingOrMoimContentContainingOrMoimNicknameContaining(String searchKeyword,
                                                                                        String searchKeyword1,
                                                                                        String moimNickname,
                                                                                        Pageable pageable);

    Page<Moim> findByMoimTitleContaining(String searchKeyword, Pageable pageable);

    Page<Moim> findByMoimContentContaining(String searchKeyword, Pageable pageable);

    Page<Moim> findByMoimNicknameContaining(String moimNickname, Pageable pageable);
}
