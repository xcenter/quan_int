package com.herendi.quan_int.controller.dto;

import com.herendi.quan_int.domain.CurrencyCode;
import com.herendi.quan_int.domain.Payment;
import com.herendi.quan_int.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        String id,
        BigDecimal amount,
        CurrencyCode currency,
        String creditorAccount,
        String debtorAccount,
        PaymentStatus status,
        Instant createdAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getCreditorAccount(),
                payment.getDebtorAccount(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }
}
