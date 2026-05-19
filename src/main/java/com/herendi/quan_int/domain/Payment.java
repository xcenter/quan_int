package com.herendi.quan_int.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency", length = 3)
    private CurrencyCode currency;

    @Column(name = "creditor_account")
    private String creditorAccount;

    @Column(name = "debtor_account")
    private String debtorAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public static Payment newPayment(BigDecimal amount, CurrencyCode currency, String creditorAccount, String debtorAccount) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setCreditorAccount(creditorAccount);
        payment.setDebtorAccount(debtorAccount);
        return payment;
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (status == null) {
            status = PaymentStatus.CREATED;
        }
    }

}
