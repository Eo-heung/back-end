package com.example.backend.entity;

import com.example.backend.configuration.ApplicationContextHolder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PostUpdate;

public class MoimRegistrationListener {

    @PostUpdate
    public void afterMoimRegUpdate(MoimRegistration moimRegistration) {
        if (moimRegistration.getRegStatus() == MoimRegistration.RegStatus.APPROVED
                || moimRegistration.getRegStatus() == MoimRegistration.RegStatus.CANCELED) {
            EntityManager entityManager = ApplicationContextHolder.getBean(EntityManager.class);
            // EntityManager를 사용하여 로직 처리
            Moim moim = entityManager.find(Moim.class, moimRegistration.getMoim().getMoimId());
            Long approvedCount = entityManager.createQuery("SELECT COUNT(*) FROM MoimRegistration mr WHERE mr.moim.moimId = :moimId AND mr.regStatus = :status", Long.class)
                    .setParameter("moimId", moimRegistration.getMoim().getMoimId())
                    .setParameter("status", MoimRegistration.RegStatus.APPROVED)
                    .getSingleResult();

            //승인된 사람 + 모임장(1)
            moim.setCurrentMoimUser(approvedCount.intValue() + 1);
        }
    }
}
