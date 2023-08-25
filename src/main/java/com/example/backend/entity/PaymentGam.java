package com.example.backend.entity;

import com.example.backend.dto.PaymentGamDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "PAYMENTGAM")
public class PaymentGam {

        @Id
        @Column(name = "id", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "userId")
        private String userId;

        @Column(name = "imp_uid")
        private String impUid;

        @Column(name = "merchant_uid")
        private String merchant_uid;

        @Column(name = "value")
        private Long value;

        @Column(name = "gotGam")
        private Long gotGam;


        @Column(name = "payDate")
        private LocalDateTime payDate;

        @Column(name = "status")
        private Boolean status;

        @Column(name = "refund")
        private Boolean refund;
        public PaymentGamDTO EntityToDTO() {
                return PaymentGamDTO.builder()
                        .user_id(this.userId)
                        .imp_uid(this.impUid)
                        .merchant_uid(this.merchant_uid)
                        .value(this.value)
                        .gotGam(this.gotGam)
                        .payDate(this.payDate)
                        .status(this.status)
                        .status(this.refund)
                        .build();
        }




    }
