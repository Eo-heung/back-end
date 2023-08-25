package com.example.backend.repository;


import com.example.backend.entity.PaymentGam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentGam, Integer> {
    List<PaymentGam> findByUserIdOrderByPayDateDesc(String userid);

    List<PaymentGam> findByUserIdAndPayDateGreaterThanEqualOrderByPayDateDesc(String userId, LocalDateTime date);
    List<PaymentGam> findByUserIdAndPayDateBeforeOrderByPayDateDesc(String userId, LocalDateTime date);

}
