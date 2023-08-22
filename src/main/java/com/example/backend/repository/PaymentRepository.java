package com.example.backend.repository;


import com.example.backend.entity.PaymentGam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentGam, Integer> {
    List<PaymentGam> findByUserId(String userid);


}
