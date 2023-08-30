package com.example.backend.service;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimPicture;

import java.util.List;

public interface MoimPictureService {

    MoimPicture createMoim(MoimPicture moimPicture);

    MoimPicture getPic(Moim moim);

    List<MoimPicture> getPictureList();



}
