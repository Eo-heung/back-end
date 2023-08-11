package com.example.backend.service;

import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MoimService {

    void createMoim(Moim moim);

    Page<Moim> listMoim(Pageable pageable, String searchCondition, User user, String searchKeyword);
}
