package com.example.backend.service.impl;

import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
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
    public Page<Moim> searchMoims(User user, int category, String keyword, String searchType, String orderBy, Pageable pageable) {
        if (category == 999 && "all".equalsIgnoreCase(searchType)) {
            if ("ascending".equals(orderBy)) {
                return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
            } else {
                return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
            }
        }

        if (category == 999) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
                    }
            }
        } else if (category == 101) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
                    }
            }
        }else if (category == 102) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
                    }
            }
        }else if (category == 103) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
                    }
            }
        }else if (category == 104) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
                    }
            }
        }else if (category == 105) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
                    }
            }
        }else if (category == 106) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
                    }
            }
        }else if (category == 107) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
                    }
            }
        }else if (category == 108) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
                    }
            }
        }else if (category == 109) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
                    }
            }
        }else if (category == 110) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
                    } else {
                        return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
                    }
            }
        }else {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndMoimTitleContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndMoimTitleContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndMoimContentContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndMoimContentContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndMoimNicknameContainingOrderByMoimIdAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndMoimNicknameContainingOrderByMoimIdDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
                    }
            }
        }
    }

}
