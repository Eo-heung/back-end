package com.example.backend.service;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MoimService {

    Moim createMoim(Moim moim, User currentUser);

    Moim modifyMoim(Moim moim);

    Moim viewMoim(int moimId);

    Page<Moim> searchMoims(User user, String category, String keyword, String searchType, String orderBy, Pageable pageable);

    Page<Moim> getMyMoim(String userId, String keyword, String orderBy, Pageable pageable);

    boolean verifyMemberRole(User user, Moim moim);
    boolean verifyLeaderRole(User user, Moim moim);

    boolean canAccessMoim(User user, Moim moim);

}
