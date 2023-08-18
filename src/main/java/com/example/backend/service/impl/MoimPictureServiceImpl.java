package com.example.backend.service.impl;

import com.example.backend.dto.MoimPictureDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimPicture;
import com.example.backend.repository.MoimPictureRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.service.MoimPictureService;
import com.example.backend.service.MoimService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoimPictureServiceImpl implements MoimPictureService {
    private final MoimRepository moimRepository;
    private final MoimPictureRepository moimPictureRepository;

    @Override
    public MoimPicture createMoim(MoimPicture moimPicture) {
        return moimPictureRepository.save(moimPicture);
    }

//    @Transactional
//    @Override
//    public void modifyPic(MoimPicture moimpicture) {
//
//        MoimPicture modifyPic = getPic(moimpicture.getMoimId());
//
//        modifyPic.setMoimPic((moimpicture.getMoimPic()));
//        modifyPic.setUpdatePic(LocalDateTime.now());
//
////        moimPictureRepository.save(moimpicture);
////        moimPictureRepository.flush();
//    }

    @Override
    public MoimPicture getPic(Moim moim) {
        return moimPictureRepository.findByMoimId(moim).get();
    }

    @Override
    public List<MoimPicture> getPictureList() {
        return moimPictureRepository.findAll();
    }


}
