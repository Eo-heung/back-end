package com.example.backend.service;

import com.example.backend.entity.Moim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface MoimService {

    Moim viewMoim(int moimId);

    void createMoim(Moim moim, MultipartFile moimPic);

    Page<Moim> listMoim(Pageable pageable, String searchCondition, String moimNickname, String searchKeyword);

    void modifyMoim(Moim moim);

    void deleteMoim(int moimId);




}
