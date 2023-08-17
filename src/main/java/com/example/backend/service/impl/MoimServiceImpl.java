package com.example.backend.service.impl;

import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import com.example.backend.repository.MoimRepository;
import com.example.backend.service.MoimService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoimServiceImpl implements MoimService {
    private final MoimRepository moimRepository;


    @Override
    public void createMoim(Moim moim) {
        moimRepository.save(moim);
        moimRepository.flush();
    }

    @Override
    public Page<Moim> listMoim(Pageable pageable, String searchCondition, User user, String searchKeyword) {
        if(searchCondition.equals("all")) {
            if(searchKeyword.equals("")) {
                return moimRepository.findAll(pageable);
            } else {
                return moimRepository.findByMoimTitleContainingOrMoimContentContainingOrUserIdContaining(searchKeyword, searchKeyword, user, pageable);
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
                    return moimRepository.findByUserIdContaining(user, pageable);
                } else {
                    return moimRepository.findAll(pageable);
                }
            }
        }
    }

}
