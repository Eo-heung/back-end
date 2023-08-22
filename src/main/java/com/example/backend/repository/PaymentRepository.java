package com.example.backend.repository;


import com.example.backend.entity.PaymentGam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentGam, Integer> {


}
