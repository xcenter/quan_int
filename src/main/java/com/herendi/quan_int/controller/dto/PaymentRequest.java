package com.herendi.quan_int.controller.dto;

import com.herendi.quan_int.domain.CurrencyCode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull(message = "Amount cannot be null") @DecimalMin(value = "0.01", message = "Amount must be greater than 0.01") BigDecimal amount,
        @NotNull(message = "Currency cannot be null") CurrencyCode currency,
        @NotBlank(message = "Creditor account cannot be blank") String creditorAccount,
        @NotBlank(message = "Debtor account cannot be blank") String debtorAccount) {
}
