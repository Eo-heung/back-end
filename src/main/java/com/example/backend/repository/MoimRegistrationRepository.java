package com.example.backend.repository;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MoimRegistrationRepository extends JpaRepository<MoimRegistration, Integer> {
    Optional<MoimRegistration> findByMoimAndUser(Moim moim, User user);

    Optional<MoimRegistration> findByUser(User user);
    List<MoimRegistration> findByMoim(Moim moim);


}
