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
    public Page<Moim> searchMoims(User user, String category, String keyword, String searchType, String orderBy, Pageable pageable) {

        System.out.println("카테고리3컨");
        System.out.println(category);
        if ("전체".equalsIgnoreCase(category) && "all".equalsIgnoreCase(searchType)) {
            if ("ascending".equals(orderBy)) {
                return moimRepository.findAllByOrderByMoimIdAsc(user, pageable);
            } else {
                return moimRepository.findAllByOrderByMoimIdDesc(user, pageable);
            }
        }

        System.out.println("카테고리4컨");
        System.out.println(category);

        if ("전체".equalsIgnoreCase(category)) {
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
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
                    }
            }
        } else if ("인문학/책".equalsIgnoreCase(category)) {
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
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
                    }
            }
        }else if ("운동".equalsIgnoreCase(category)) {
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
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
                    }
            }
        }else if ("요리/맛집".equalsIgnoreCase(category)) {
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
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
                    }
            }
        }else if ("공예/만들기".equalsIgnoreCase(category)) {
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
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
                    }
            }
        }else if ("원예".equalsIgnoreCase(category)) {
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
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
                    }
            }
        }else if ("동네친구".equalsIgnoreCase(category)) {
            System.out.println(category);

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
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
                    }
            }
        }else if ("음악/악기".equalsIgnoreCase(category)) {
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
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
                    }
            }
        }else if ("반려동물".equalsIgnoreCase(category)) {
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
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
                    }
            }
        }else if ("여행".equalsIgnoreCase(category)) {
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
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
                    }
            }
        }else if ("문화/여가".equalsIgnoreCase(category)) {
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
                        return moimRepository.findByMoimCategoryOrderByMoimIdAsc(user, category, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryOrderByMoimIdDesc(user, category, pageable);
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
