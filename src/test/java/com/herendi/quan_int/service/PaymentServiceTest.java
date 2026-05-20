package com.herendi.quan_int.service;

import com.herendi.quan_int.controller.dto.PaymentRequest;
import com.herendi.quan_int.domain.CurrencyCode;
import com.herendi.quan_int.domain.Payment;
import com.herendi.quan_int.domain.PaymentStatus;
import com.herendi.quan_int.exception.PaymentNotFoundException;
import com.herendi.quan_int.repository.PaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    public static final String CREDITOR = "Creditor";
    public static final String DEBTOR = "Debtor";
    public static final String CREDITOR_2 = "Creditor2";
    public static final String DEBTOR_2 = "Debtor2";
    PaymentRequest paymentRequest;
    Payment createdPayment;
    Payment completedPayment;
    Payment failedPayment;

    PaymentService underTest;

    @Mock
    PaymentRepository mockPaymentRepository;

    static Payment paymentProvider(BigDecimal value, CurrencyCode currencyCode, String creditorAccount, String debtorAccount, PaymentStatus status) {
        Payment payment = Payment.newPayment(value, currencyCode, creditorAccount, debtorAccount);
        payment.setStatus(status);
        payment.setId("123");
        payment.setCreatedAt(Instant.now());
        return payment;
    }

    @BeforeEach
    void setUp() {
        underTest = new PaymentService(mockPaymentRepository);
        createdPayment = paymentProvider(BigDecimal.TEN, CurrencyCode.EUR, CREDITOR, DEBTOR, PaymentStatus.CREATED);
        completedPayment = paymentProvider(BigDecimal.TEN, CurrencyCode.EUR, CREDITOR, DEBTOR, PaymentStatus.COMPLETED);
        failedPayment = paymentProvider(BigDecimal.TEN, CurrencyCode.EUR, CREDITOR, DEBTOR, PaymentStatus.FAILED);
        paymentRequest = new PaymentRequest(BigDecimal.ONE, CurrencyCode.USD, CREDITOR_2, DEBTOR_2);
    }

    @Test
    void testCreatePaymentCallsRepository() {

        underTest.createPayment(BigDecimal.TEN, CurrencyCode.EUR, CREDITOR, DEBTOR);

        verify(mockPaymentRepository).save(Mockito.argThat(payment -> payment.getAmount().equals(BigDecimal.TEN) &&
                payment.getCurrency().equals(CurrencyCode.EUR) &&
                payment.getCreditorAccount().equals(CREDITOR) &&
                payment.getDebtorAccount().equals(DEBTOR)));
    }

    @Test
    void updatePaymentThrowsIllegalStateExceptionWhenPaymentInWrongStatus() {
        when(mockPaymentRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(completedPayment));

        assertThrows(IllegalStateException.class, () -> underTest.updatePayment(completedPayment.getId(), paymentRequest));
    }

    @Test
    void updatePaymentSucceedsWhenPaymentInCreatedStatus() {
        Payment resultingPayment = paymentProvider(BigDecimal.ONE, CurrencyCode.USD, CREDITOR_2, DEBTOR_2, PaymentStatus.CREATED);
        when(mockPaymentRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(createdPayment));
        when(mockPaymentRepository.save(Mockito.any(Payment.class))).thenReturn(resultingPayment);

        Payment result = underTest.updatePayment(createdPayment.getId(), paymentRequest);
        Assertions.assertEquals(resultingPayment, result);
    }

    @Test
    void updatePaymentThrowsPaymentNotFoundExceptionIfPaymentCantBeFound() {
        when(mockPaymentRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> underTest.updatePayment("nonexistent-id", paymentRequest));
    }

    @Test
    void deletePaymentThrowsIllegalStateExceptionWhenPaymentInWrongStatus() {
        when(mockPaymentRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(completedPayment));

        assertThrows(IllegalStateException.class, () -> underTest.deletePayment(completedPayment.getId()));
    }

    @Test
    void completePaymentThrowsIllegalStateExceptionWhenPaymentInWrongStatus() {
        when(mockPaymentRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(completedPayment));

        assertThrows(IllegalStateException.class, () -> underTest.completePayment(completedPayment.getId()));
    }

    @Test
    void completePaymentSucceedsWhenPaymentInCreatedStatus() {
        Payment resultingPayment = paymentProvider(BigDecimal.ONE, CurrencyCode.USD, CREDITOR_2, DEBTOR_2, PaymentStatus.COMPLETED);
        when(mockPaymentRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(createdPayment));
        when(mockPaymentRepository.save(Mockito.any(Payment.class))).thenReturn(resultingPayment);
        Payment result = underTest.completePayment(createdPayment.getId());
        Assertions.assertEquals(resultingPayment, result);
    }

    @Test
    void failPaymentThrowsIllegalStateExceptionWhenPaymentInWrongStatus() {
        when(mockPaymentRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(completedPayment));

        assertThrows(IllegalStateException.class, () -> underTest.failPayment(completedPayment.getId()));
    }

    @Test
    void failPaymentSucceedsWhenPaymentInCreatedStatus() {
        Payment resultingPayment = paymentProvider(BigDecimal.ONE, CurrencyCode.USD, CREDITOR_2, DEBTOR_2, PaymentStatus.COMPLETED);
        when(mockPaymentRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(createdPayment));
        when(mockPaymentRepository.save(Mockito.any(Payment.class))).thenReturn(resultingPayment);
        Payment result = underTest.failPayment(createdPayment.getId());
        Assertions.assertEquals(resultingPayment, result);
    }
}