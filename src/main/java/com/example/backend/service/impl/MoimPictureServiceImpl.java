package com.example.backend.service.impl;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimPicture;
import com.example.backend.repository.MoimPictureRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.service.MoimPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoimPictureServiceImpl implements MoimPictureService {
    private final MoimPictureRepository moimPictureRepository;

    @Override
    @Transactional
    public MoimPicture createMoim(MoimPicture moimPicture) {
        return moimPictureRepository.save(moimPicture);
    }

    @Override
    public MoimPicture getPic(Moim moim) {
        return moimPictureRepository.findByMoimId(moim).get();
    }

    @Override
    public List<MoimPicture> getPictureList() {
        return moimPictureRepository.findAll();
    }



}
