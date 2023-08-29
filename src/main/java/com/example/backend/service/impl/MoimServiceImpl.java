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

        System.out.println("카테고리4컨");
        System.out.println(category);
        System.out.println(searchType);
        System.out.println(keyword);

        if ("전체".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByAllAndMoimTitleAsc(user, keyword, pageable);
                    } else {
                        return moimRepository.findByAllAndMoimTitleDesc(user, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByAllAndMoimContentAsc(user, keyword, pageable);
                    } else {
                        return moimRepository.findByAllAndMoimContentDesc(user, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByAllAndMoimNicknameAsc(user, keyword, pageable);
                    } else {
                        return moimRepository.findByAllAndMoimNicknameDesc(user, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByAllAsc(user, keyword, pageable);
                    } else {
                        return moimRepository.findByAllDesc(user, keyword, pageable);
                    }
            }
        } else if ("인문학/책".equalsIgnoreCase(category)) {

            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimTitleAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimTitleDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimContentAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimContentDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimNicknameAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimNicknameDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndAllAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndAllDesc(user, category, keyword, pageable);
                    }
            }
        }else if ("운동".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimTitleAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimTitleDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimContentAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimContentDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimNicknameAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimNicknameDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndAllAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndAllDesc(user, category, keyword, pageable);
                    }
            }
        }else if ("요리/맛집".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimTitleAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimTitleDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimContentAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimContentDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimNicknameAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimNicknameDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndAllAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndAllDesc(user, category, keyword, pageable);
                    }
            }
        }else if ("공예/만들기".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimTitleAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimTitleDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimContentAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimContentDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimNicknameAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimNicknameDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndAllAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndAllDesc(user, category, keyword, pageable);
                    }
            }
        }else if ("원예".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimTitleAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimTitleDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimContentAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimContentDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimNicknameAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimNicknameDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndAllAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndAllDesc(user, category, keyword, pageable);
                    }
            }
        }else if ("동네친구".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimTitleAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimTitleDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimContentAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimContentDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimNicknameAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimNicknameDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndAllAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndAllDesc(user, category, keyword, pageable);
                    }
            }
        }else if ("음악/악기".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimTitleAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimTitleDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimContentAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimContentDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimNicknameAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimNicknameDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndAllAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndAllDesc(user, category, keyword, pageable);
                    }
            }
        }else if ("반려동물".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimTitleAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimTitleDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimContentAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimContentDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimNicknameAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimNicknameDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndAllAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndAllDesc(user, category, keyword, pageable);
                    }
            }
        }else if ("여행".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimTitleAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimTitleDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimContentAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimContentDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimNicknameAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimNicknameDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndAllAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndAllDesc(user, category, keyword, pageable);
                    }
            }
        }else if ("문화/여가".equalsIgnoreCase(category)) {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimTitleAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimTitleDesc(user, category, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimContentAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimContentDesc(user, category, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByCategoryAndMoimNicknameAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByCategoryAndMoimNicknameDesc(user, category, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByMoimCategoryAndAllAsc(user, category, keyword, pageable);
                    } else {
                        return moimRepository.findByMoimCategoryAndAllDesc(user, category, keyword, pageable);
                    }
            }
        }else {
            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByAllAndMoimTitleAsc(user, keyword, pageable);
                    } else {
                        return moimRepository.findByAllAndMoimTitleDesc(user, keyword, pageable);
                    }
                case "content":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByAllAndMoimContentAsc(user, keyword, pageable);
                    } else {
                        return moimRepository.findByAllAndMoimContentDesc(user, keyword, pageable);
                    }
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByAllAndMoimNicknameAsc(user, keyword, pageable);
                    } else {
                        return moimRepository.findByAllAndMoimNicknameDesc(user, keyword, pageable);
                    }
                default:
                    if ("ascending".equals(orderBy)) {
                        return moimRepository.findByAllAsc(user, keyword, pageable);
                    } else {
                        return moimRepository.findByAllDesc(user, keyword, pageable);
                    }
            }
        }
    }

    public Page<Moim> getMyMoim(String userId, String keyword,  String orderBy, Pageable pageable) {
        System.out.println("5트");
        if ("ascending".equalsIgnoreCase(orderBy)) {
            System.out.println("6트");
            System.out.println(keyword);
            return moimRepository.findmyMoimAsc(userId, keyword, pageable);
        } else {
            return moimRepository.findmyMoimDesc(userId, keyword, pageable);

        }
    }
}
