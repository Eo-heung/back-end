package com.example.backend.service.impl;

import com.example.backend.dto.MoimRegistrationDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.entity.ProfileImage;
import com.example.backend.entity.User;
import com.example.backend.repository.MoimRegistrationRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.repository.ProfileImageRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.MoimRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoimRegistrationServiceImpl implements MoimRegistrationService {
    private final UserRepository userRepository;
    private final MoimRepository moimRepository;
    private final MoimRegistrationRepository moimRegistrationRepository;
    private final ProfileImageRepository profileImageRepository;

    //모임 가입 신청
    @Override
    public MoimRegistration applyToMoim(int moimId, String userId,
                                        MultipartFile moimProfile) {

        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다"));

        if(moim.getUserId().getUserId().equals(userId)) {
            throw new IllegalArgumentException("모임장은 모임에 가입할 수 없습니다.");
        }

        Optional<MoimRegistration> existingRegistration = moimRegistrationRepository.findByMoimAndUser(moim, user);
        // 가입 가능 여부 및 CANCELED 상태 확인
        if (existingRegistration.isPresent()) {
            MoimRegistration registration = existingRegistration.get();

            if (registration.getRegStatus() == MoimRegistration.RegStatus.CANCELED) {
                // CANCELED 상태일 경우, 상태를 Waiting으로 업데이트하고 반환
                registration.setRegStatus(MoimRegistration.RegStatus.WAITING);
                registration.setApplicationDate(LocalDateTime.now());
                return moimRegistrationRepository.save(registration);
            } else {
                throw new IllegalArgumentException("이미 가입 신청하거나 가입된 상태입니다.");
            }
        }

        ProfileImage profileImage = profileImageRepository.findByUserId(user);

        if (profileImage == null) {
            throw new EntityNotFoundException("프로필 이미지를 찾을 수 없습니다.");
        }

        byte[] newBytes = null;

        if(moimProfile != null && !moimProfile.isEmpty()) {
            //새로운 모임 프로필 등록
            try {
                newBytes = moimProfile.getBytes();
            } catch (IOException ioe) {
                throw new RuntimeException(ioe.getMessage(), ioe);
            }
        }

        MoimRegistration moimReg = MoimRegistration.builder()
                .moim(moim)
                .user(user)
                .moimProfile(newBytes)
                .createMoimProfile(LocalDateTime.now())
                .regStatus(MoimRegistration.RegStatus.WAITING)
                .applicationDate(LocalDateTime.now())
                .build();

        return moimRegistrationRepository.save(moimReg);
    }

    //가입 신청 취소
    @Override
    public MoimRegistration cancelMoim(int moimId, String userId) {
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다"));

        MoimRegistration existingRegistration = moimRegistrationRepository.findByMoimAndUser(moim, user)
                .orElseThrow(() -> new IllegalArgumentException("가입 신청하지 않은 모임입니다."));

        existingRegistration.setRegStatus(MoimRegistration.RegStatus.CANCELED);
        return moimRegistrationRepository.save(existingRegistration);
    }

    //(모임장) 가입 승인
    @Override
    public MoimRegistration approveMoim(int moimId, String applicantUserId, String organizerUserId) {
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다."));
        User applicant = userRepository.findById(applicantUserId)
                .orElseThrow(() -> new EntityNotFoundException("신청자를 찾을 수 없습니다."));

        // 모임장 확인을 위해 로그인 사용자 대신 organizerUserId 사용
        if(!moim.getUserId().getUserId().equals(organizerUserId)) {
            throw new AccessDeniedException("모임장만 승인할 수 있습니다.");
        }

        MoimRegistration existingRegistration = moimRegistrationRepository.findByMoimAndUser(moim, applicant)
                .orElseThrow(() -> new IllegalArgumentException("신청한 사용자가 없습니다."));

        existingRegistration.setRegStatus(MoimRegistration.RegStatus.APPROVED);

        // 가입일을 현재 날짜 및 시간으로 설정
        existingRegistration.setSubscribeDate(LocalDateTime.now());

        moim.incrementCurrentMoimUser(); // 가입자 수 증가
        moimRepository.save(moim);

        return moimRegistrationRepository.save(existingRegistration);
    }

    //(모임장) 가입 거절
    @Override
    public MoimRegistration rejectMoim(int moimId, String applicantUserId, String organizerUserId) {
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다."));
        User applicant = userRepository.findById(applicantUserId)
                .orElseThrow(() -> new EntityNotFoundException("신청자를 찾을 수 없습니다."));

        if (!moim.getUserId().getUserId().equals(organizerUserId)) {
            throw new AccessDeniedException("모임장만 거절할 수 있습니다.");
        }

        MoimRegistration existingRegistration = moimRegistrationRepository.findByMoimAndUser(moim, applicant)
                .orElseThrow(() -> new IllegalArgumentException("가입 신청하지 않은 모임입니다."));

        existingRegistration.setRegStatus(MoimRegistration.RegStatus.REJECTED); // 거절 상태로 변경
        moimRegistrationRepository.save(existingRegistration);

        return existingRegistration;
    }

    //(모임원) 모임 탈퇴
    @Override
    public MoimRegistration quitMoim(int moimId, String applicantUserId) {
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다."));
        User user = userRepository.findById(applicantUserId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다"));

        MoimRegistration existingRegistration = moimRegistrationRepository.findByMoimAndUser(moim, user)
                .orElseThrow(() -> new IllegalArgumentException("가입 신청하지 않은 모임입니다."));

        existingRegistration.setRegStatus(MoimRegistration.RegStatus.QUIT);

        // 탈퇴일
        existingRegistration.setQuitDate(LocalDateTime.now());
        moim.decrementCurrentMoimUser();
        moimRepository.save(moim);

        return moimRegistrationRepository.save(existingRegistration);
    }

    //(모임원) 모임 프로필 수정
    @Override
    public MoimRegistration modifyMoimProfile(int moimId, String userId, MultipartFile moimProfile) {
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다"));

        //모임 가입여부 확인
        Optional<MoimRegistration> moimChk = moimRegistrationRepository.findByMoimAndUser(moim, user);

        if (!moimChk.isPresent()) {
            throw new IllegalArgumentException("모임에 가입하지 않은 사용자입니다.");
        }

        MoimRegistration moimReg = moimChk.get();

        try {
            moimReg.setMoimProfile(moimProfile.getBytes());
            moimReg.setUpdateMoimProfile(LocalDateTime.now());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage(), ioe);
        }

        return moimRegistrationRepository.save(moimReg);
    }

    //(모임장) 신청자 리스트
    @Override
    public Page<MoimRegistrationDTO> getApplicantList(
                                                      int moimId,
                                                      String organizerUserId,
                                                      String applicantUserNickname,
                                                      String orderBy,
                                                      Pageable pageable) {
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다."));

        if (!moim.getUserId().getUserId().equals(organizerUserId)) {
            throw new AccessDeniedException("모임장만 접근할 수 있습니다.");
        }

        if (applicantUserNickname == null || applicantUserNickname.trim().isEmpty()) {
            applicantUserNickname = "";
        }

        Page<MoimRegistration> moimRegList = null;
        System.out.println("orderBy");
        System.out.println(orderBy);
        if ("ascending".equals(orderBy)) {
            moimRegList = moimRegistrationRepository.findApplicantsByMoimIdAndUserIdOrderByMoimIdAsc(moimId, organizerUserId, pageable);
            System.out.println(moimRegList);
        } else {
            moimRegList = moimRegistrationRepository.findApplicantsByMoimIdAndUserIdOrderByMoimIdDesc(moimId, organizerUserId, pageable);
        }

        return moimRegList.map(MoimRegistration::toDTO);
    }

    //(모임장) 신청자 상세 내용
    @Override
    public MoimRegistrationDTO getApplicant(int moimId, int moimRegId, String organizerUserId) {
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다."));

        if (!moim.getUserId().getUserId().equals(organizerUserId)) {
            throw new AccessDeniedException("모임장만 접근할 수 있습니다.");

        }

        MoimRegistration moimRegistration = moimRegistrationRepository.findById(moimRegId)
                .orElseThrow(() -> new EntityNotFoundException("신청자 정보를 찾을 수 없습니다."));

        if (!moim.equals(moimRegistration.getMoim())) {
            throw new EntityNotFoundException("해당 모임에 신청자 정보가 존재하지 않습니다.");
        }

        return moimRegistration.toDTO();
    }

    //모임프로필 사진 및 정보 받아오기(moimID로)
    @Override
    public MoimRegistrationDTO getApplicantByMoimId(int moimId, String loginUser) {
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다."));

        User user = userRepository.findById(loginUser)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        if (verifyLeaderRole(user, checkMoim) || verifyMemberRole(user, checkMoim)) {
            // 모임ID에 해당하는 신청서 찾기
            MoimRegistration moimRegistration = moimRegistrationRepository.findByMoim(checkMoim)
                    .orElseThrow(() -> new EntityNotFoundException("신청자 정보를 찾을 수 없습니다."));

            return moimRegistration.toDTOforBase64();
        } else {
            throw new IllegalStateException("이 사용자는 이 게시물을 보는 권한이 없습니다.");
        }



    }

    public Optional<MoimRegistrationDTO> getMyApplyId(int moimId, String loginUser) {
        User user = userRepository.findById(loginUser)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        return moimRegistrationRepository.findByUserAndMoim_MoimId(user, moimId).map(MoimRegistration::toDTO);
    }


    public boolean verifyMemberRole(User user, Moim moim) {

        Optional<MoimRegistration> optionalRegistration = moimRegistrationRepository.findByMoimAndUser(moim, user);

        if (!optionalRegistration.isPresent()) {
            return false; // 모임에 등록되지 않은 사용자
        }

        MoimRegistration registration = optionalRegistration.get();
        return registration.getRegStatus() == MoimRegistration.RegStatus.APPROVED;
    }

    public boolean verifyLeaderRole(User user, Moim moim) {
        return user.equals(moim.getUserId());
    }


}
