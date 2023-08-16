package com.example.backend.repository;


import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimRepository extends JpaRepository<Moim, Integer> {


    Page<Moim> findByMoimTitleContainingOrMoimContentContainingOrUserIdContaining(String searchKeyword,
                                                                                 String searchKeyword1,
                                                                                 User user, Pageable pageable);


    Page<Moim> findByMoimTitleContaining(String searchKeyword, Pageable pageable);

    Page<Moim> findByMoimContentContaining(String searchKeyword, Pageable pageable);

    Page<Moim> findByUserIdContaining(User user, Pageable pageable);
}
