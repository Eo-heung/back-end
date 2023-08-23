package com.example.backend.controller;

import com.example.backend.dto.PaymentGamDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.PaymentGam;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final IamportClient iamportClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public PaymentController(JwtTokenProvider jwtTokenProvider, PaymentRepository paymentRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.iamportClient = new IamportClient("8106611653013332", "AfZoYvt3SkS88yRfzmqB7IT1Np6MAwxIa9PKkW367CZu5xUU3bGFvZr65LCCoC0gtQqyAkAYZiDeFN0W");
        this.restTemplate = restTemplate;
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

    @PostMapping("/paymentList")
    public ResponseEntity<?> PaymentList(@RequestHeader("Authorization") String token) {
        System.out.println("#################");
        ResponseDTO<PaymentGam> responseDTO = new ResponseDTO<>();
        String userId = jwtTokenProvider.validateAndGetUsername(token);

        List<PaymentGam> payments = paymentRepository.findByUserId(userId);

        responseDTO.setItems(payments);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/cancelPayment/{id}")
    public ResponseEntity<?> cancelPayment(@PathVariable String id) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

        try {
            String accessToken = getToken();

            String url = "https://api.iamport.kr/payments/cancel";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            Map<String, String> body = new HashMap<>();
            body.put("imp_uid", id);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

            CancelResponse response = restTemplate.postForObject(url, requestEntity, CancelResponse.class);
            Map<String, Object> returnMap = new HashMap<>();

            if (response.getCode() == 0) {
                returnMap.put("msg", "취소가 완료되었습니다.");

                responseDTO.setItem(returnMap);
                responseDTO.setStatusCode(HttpStatus.OK.value());
                return ResponseEntity.ok().body(responseDTO);
            } else {
                returnMap.put("msg", "결제 취소가 실패하였습니다.");

                responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
                responseDTO.setErrorMessage(response.getMessage());
                responseDTO.setItem(returnMap);

                return ResponseEntity.badRequest().body(responseDTO);
            }

        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    public static String getToken() {
        RestTemplate restTemplate = new RestTemplate();

        // API endpoint
        String url = "https://api.iamport.kr/users/getToken";

        // REST API 키와 REST API Secret를 설정
        Map<String, String> body = new HashMap<>();
        body.put("imp_key", "8106611653013332");
        body.put("imp_secret", "AfZoYvt3SkS88yRfzmqB7IT1Np6MAwxIa9PKkW367CZu5xUU3bGFvZr65LCCoC0gtQqyAkAYZiDeFN0W");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);
        TokenResponse response = restTemplate.postForObject(url, requestEntity, TokenResponse.class);

        System.out.println("Access Token: " + response.getResponse().getAccess_token());

        return response.getResponse().getAccess_token();
    }

    @Data
    public static class TokenResponse {
        private int code;
        private String message;
        private TokenDetails response;
    }
    @Data
    public static class TokenDetails {
        private String access_token;
        private long now;
        private long expired_at;
    }
    @Data
    public static class CancelResponse {
        private int code;
        private String message;
        private Map<String, Object> response;
    }

}