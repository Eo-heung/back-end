package com.example.backend.service;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimPicture;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MoimPictureService {

    MoimPicture createMoim(MoimPicture moimPicture);

//    void modifyPic(MoimPicture moimPicture);

    MoimPicture getPic(Moim moim);

    List<MoimPicture> getPictureList();

}
