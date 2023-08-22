package com.example.backend.controller;

import com.example.backend.dto.PaymentGamDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.PaymentGam;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

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

    @Autowired
    public PaymentController(JwtTokenProvider jwtTokenProvider, PaymentRepository paymentRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.paymentRepository = paymentRepository;
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
        paymentRepository.save(paymentGam);


        responseDTO.setItem(paymentGam.EntityToDTO());
        return ResponseEntity.ok(responseDTO);
    }

}