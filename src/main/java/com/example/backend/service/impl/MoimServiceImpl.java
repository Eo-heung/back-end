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
    public Page<Moim> searchMoims(String category, String keyword, String searchType, String orderBy, Pageable pageable) {
        if ("all".equalsIgnoreCase(category) && "all".equalsIgnoreCase(searchType)) {
            if ("ascending".equals(orderBy)) {
                return moimRepository.findAllByOrderByMoimIdAsc(pageable);
            } else {
                return moimRepository.findAllByOrderByMoimIdDesc(pageable);
            }
        }

        if ("all".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(pageable);
                    }
            }
        } else {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndMoimTitleContainingOrderByMoimIdAsc(category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndMoimTitleContainingOrderByMoimIdDesc(category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndMoimContentContainingOrderByMoimIdAsc(category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndMoimContentContainingOrderByMoimIdDesc(category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndMoimNicknameContainingOrderByMoimIdAsc(category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndMoimNicknameContainingOrderByMoimIdDesc(category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(category, pageable);
                    }
            }
        }
    }

}
