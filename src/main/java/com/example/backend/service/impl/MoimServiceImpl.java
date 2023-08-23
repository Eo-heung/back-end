package com.example.backend.service.impl;

import com.example.backend.entity.Moim;
import com.example.backend.repository.MoimPictureRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.MoimService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoimServiceImpl implements MoimService {
    private final MoimRepository moimRepository;

    @Override
    public Moim viewMoim(int moimId) {
        if (moimRepository.findById(moimId).isEmpty())
            return null;
        else
            return moimRepository.findById(moimId).get();
    }

    @Override
    @Transactional
    public Moim createMoim(Moim moim) {
        return moimRepository.save(moim);
    }

    @Override
    @Transactional
    public Moim modifyMoim(Moim moim) {
        return moimRepository.save(moim);
    }

    @Override
    public List<Moim> getMoimList() {
        return moimRepository.findAll();
    }

    @Override
    public Page<Moim> searchMoims(String category, String keyword, String searchType, Pageable pageable) {
        if ("all".equalsIgnoreCase(category) && "all".equalsIgnoreCase(searchType)) {
            return moimRepository.findAll(pageable);
        }

        if ("all".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    return moimRepository.findByMoimTitleContaining(keyword, pageable);
                case "content":
                    return moimRepository.findByMoimContentContaining(keyword, pageable);
                case "nickname":
                    return moimRepository.findByMoimNicknameContaining(keyword, pageable);
                default:
                    return moimRepository.findAll(pageable);
            }
        } else {
            switch (searchType) {
                case "title":
                    return moimRepository.findByMoimCategoryAndMoimTitleContaining(category, keyword, pageable);
                case "content":
                    return moimRepository.findByMoimCategoryAndMoimContentContaining(category, keyword, pageable);
                case "nickname":
                    return moimRepository.findByMoimCategoryAndMoimNicknameContaining(category, keyword, pageable);
                default:
                    return moimRepository.findByMoimCategory(category, pageable);
            }
        }
    }

}
