package com.herendi.quan_int.controller.dto;

import com.herendi.quan_int.domain.CurrencyCode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull CurrencyCode currency,
        @NotBlank String creditorAccount,
        @NotBlank String debtorAccount) {
}
