package com.example.backend.service.impl;

import com.example.backend.entity.Moim;
import com.example.backend.repository.MoimRepository;
import com.example.backend.service.MoimService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoimServiceImpl implements MoimService {
    private final MoimRepository moimRepository;


    @Value("${file.path}")
    String savePicPath;

    @Override
    public Moim viewMoim(int moimId) {
        if(moimRepository.findById(moimId).isEmpty()) {
            return null;
        } else
        return moimRepository.findById(moimId).get();
    }

    @Override
    public void createMoim(Moim moim, MultipartFile moimPic) {
        try {

            File directory = new File(savePicPath);


            if (!directory.exists()) {
                directory.mkdirs();
            }
            System.out.println(moimPic);
            // 파일명 생성
            String originPicName = moimPic.getOriginalFilename();
            String savedPicName = UUID.randomUUID().toString() + "_" + originPicName;

            // 파일 경로
            String picPath = savePicPath + "/" + savedPicName;

            // 이미지 파일 저장
            moimPic.transferTo(new File(picPath));

            // 이미지 경로 설정
            moim.setPicPath(picPath);

            // Moim 엔터티 저장
            moimRepository.save(moim);
            moimRepository.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이미지 저장에 실패했습니다.");
        }

    }

    @Override
    public Page<Moim> listMoim(Pageable pageable, String searchCondition, String moimNickname, String searchKeyword) {
        if(searchCondition.equals("all")) {
            if(searchKeyword.equals("")) {
                return moimRepository.findAll(pageable);
            } else {
                return moimRepository.findByMoimTitleContainingOrMoimContentContainingOrMoimNicknameContaining(searchKeyword, searchKeyword, moimNickname, pageable);
            }
        } else {
            if(searchKeyword.equals("")) {
                return moimRepository.findAll(pageable);
            } else {
                if(searchCondition.equals("title")) {
                    return moimRepository.findByMoimTitleContaining(searchKeyword, pageable);
                } else if(searchCondition.equals("content")) {
                    return moimRepository.findByMoimContentContaining(searchKeyword, pageable);
                } else if(searchCondition.equals("writer")) {
                    return moimRepository.findByMoimNicknameContaining(moimNickname, pageable);
                } else {
                    return moimRepository.findAll(pageable);
                }
            }
        }
    }

    @Override
    public void modifyMoim(Moim moim) {
        moimRepository.save(moim);
        moimRepository.flush();
    }

    @Override
    public void deleteMoim(int moimId) {
        moimRepository.deleteById(moimId);
    }



}
