package com.example.backend.service;

import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface MoimService {

    Moim viewMoim(int moimId);

    Moim createMoim(Moim moim);

    Moim modifyMoim(Moim moim);

    List<Moim> getMoimList();

    Page<Moim> searchMoims(User user, String category, String keyword, String searchType, String orderBy, Pageable pageable);


}
