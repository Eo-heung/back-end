package com.example.backend.dto;

import com.example.backend.entity.PaymentGam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentGamDTO {

    private String user_id;
    private String imp_uid;
    private String merchant_uid;
    private Long value;
    private Long gotGam;
    private LocalDateTime payDate;
    private Boolean status;

    public PaymentGam DTOToEntity() {
        return PaymentGam.builder()
                .userId(this.user_id)
                .imp_uid(this.imp_uid)
                .merchant_uid(this.merchant_uid)
                .value(this.value)
                .gotGam(this.gotGam)
                .payDate(this.payDate)
                .status(this.status)
                .build();
    }
}
