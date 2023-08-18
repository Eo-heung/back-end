package com.example.backend.service.impl;

import com.example.backend.dto.MoimDTO;
import com.example.backend.dto.MoimPictureDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimPicture;
import com.example.backend.entity.User;
import com.example.backend.repository.MoimPictureRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.MoimService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoimServiceImpl implements MoimService {
    private final MoimRepository moimRepository;
    private final MoimPictureRepository moimPictureRepository;
    private final UserRepository userRepository;


    @Override
    public Moim viewMoim(int moimId) {
        if(moimRepository.findById(moimId).isEmpty()) {
            return null;
        } else
            return moimRepository.findById(moimId).get();
    }

    @Override
    @Transactional
    public Moim createMoim(Moim moim) {
        System.out.println(moim);

        return moimRepository.save(moim);
    }

    @Override
    public void modifyMoim(Moim moim) {

    }

//    @Transactional
//    @Override
//    public void modifyMoim(Moim moim) {
//        User user = userRepository.findByUserId(moimDTO.getUserId()).get();
//
//        //유저 정보를 받아와서 그 정보를 바탕으로 Moim을 찾아냄.
//        Moim writeMoim = MoimService.viewMoim(moimId);
//
////         카테고리, 유저아이디, 리더 닉네임, addr, title, maxuser, content, pic
//        writeMoim.setMoimTitle(moimDTO.getMoimTitle());
//        writeMoim.setMoimCategory(moimDTO.getMoimCategory());
//        writeMoim.setMaxMoimUser(moimDTO.getMaxMoimUser());
//        writeMoim.setMoimContent(moimDTO.getMoimContent());
//        writeMoim.setmoimAddr(moimDTO.getmoimAddr());
//
//        moimRepository.save(moim);
//        moimRepository.flush();
//    }

    @Override
    public void deleteMoim(int moimId) {
        moimRepository.deleteById(moimId);
    }

    @Override
    public List<Moim> getMoimList() {
        return moimRepository.findAll();
    }


}
