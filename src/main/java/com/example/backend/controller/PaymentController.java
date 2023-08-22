package com.example.backend.controller;

import com.example.backend.dto.PaymentGamDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.PaymentGam;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final IamportClient iamportClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentController(JwtTokenProvider jwtTokenProvider, PaymentRepository paymentRepository, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.iamportClient = new IamportClient("8106611653013332", "AfZoYvt3SkS88yRfzmqB7IT1Np6MAwxIa9PKkW367CZu5xUU3bGFvZr65LCCoC0gtQqyAkAYZiDeFN0W");
    }

    @PostMapping("/verifyIamport/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable String imp_uid) throws IamportResponseException, IOException {
        log.info("paymentByImpUid 진입" + imp_uid);
        return iamportClient.paymentByImpUid(imp_uid);
    }

    @PostMapping("/addPayment")
    public ResponseEntity<?> addPayment(@RequestHeader("Authorization") String token, @RequestBody PaymentGamDTO paymentGamDTO) {
        ResponseDTO<PaymentGamDTO> responseDTO = new ResponseDTO<>();
        String userId = jwtTokenProvider.validateAndGetUsername(token);

        PaymentGam paymentGam = paymentGamDTO.DTOToEntity();
        paymentGam.setPayDate(LocalDateTime.now());
        paymentGam.setGotGam(paymentGam.getValue()/1000);
        paymentGam.setStatus(true);
        paymentGam.setUserId(userId);
        paymentRepository.save(paymentGam);

        List<PaymentGam> payments = paymentRepository.findByUserId(userId);

        // gotGam 값들의 합 계산
        Long totalGotGam = payments.stream()
                .mapToLong(PaymentGam::getGotGam)
                .sum();
        User user = userRepository.findByUserId(userId).get();
        if (user != null) {
            user.setTotalGam(totalGotGam); // totalGam 값 업데이트
            userRepository.save(user); // 변경된 값을 데이터베이스에 저장
        } else {
            // 유저가 존재하지 않는 경우, 적절한 예외 처리나 로깅을 해주세요.
            System.out.println("User with userId " + userId + " not found.");
        }
        System.out.println("Total gotGam for user " + userId + ": " + totalGotGam);
        System.out.println(payments);
        responseDTO.setItem(paymentGam.EntityToDTO());
        return ResponseEntity.ok(responseDTO);
    }

}