package com.herendi.quan_int.service;

import com.herendi.quan_int.controller.dto.PaymentRequest;
import com.herendi.quan_int.domain.CurrencyCode;
import com.herendi.quan_int.domain.Payment;
import com.herendi.quan_int.domain.PaymentStatus;
import com.herendi.quan_int.exception.PaymentNotFoundException;
import com.herendi.quan_int.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(BigDecimal amount, CurrencyCode currency, String creditorAccount, String debtorAccount) {
        return paymentRepository.save(Payment.newPayment(amount, currency, creditorAccount, debtorAccount));
    }

    public Payment getPaymentById(String id) {
        return findExistingPaymentById(id);
    }

    @Transactional
    public Payment updatePayment(String id, PaymentRequest paymentRequest) {

        Payment payment = findExistingPaymentById(id);
        if (payment.getStatus() == PaymentStatus.COMPLETED || payment.getStatus() == PaymentStatus.FAILED) {
            throw new IllegalStateException("Cannot update payment with status COMPLETED or FAILED");
        }
        payment.setAmount(paymentRequest.amount());
        payment.setCurrency(paymentRequest.currency());
        payment.setCreditorAccount(paymentRequest.creditorAccount());
        payment.setDebtorAccount(paymentRequest.debtorAccount());
        return paymentRepository.save(payment);
    }

    @Transactional
    public void deletePayment(String id) {
        Payment payment = findExistingPaymentById(id);
        if (payment.getStatus() != PaymentStatus.CREATED) {
            throw new IllegalStateException("Cannot delete payment with status other than CREATED");
        }
        paymentRepository.deleteById(id);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Transactional
    public Payment completePayment(String id) {
        Payment payment = findExistingPaymentById(id);
        if (payment.getStatus() != PaymentStatus.CREATED) {
            throw new IllegalStateException("only payments in status CREATED may be moved to COMPLETED");
        }
        payment.setStatus(PaymentStatus.COMPLETED);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment failPayment(String id) {
        Payment payment = findExistingPaymentById(id);
        if (payment.getStatus() != PaymentStatus.CREATED) {
            throw new IllegalStateException("only payments in status CREATED may be moved to FAILED");
        }
        payment.setStatus(PaymentStatus.FAILED);
        return paymentRepository.save(payment);
    }

    private Payment findExistingPaymentById(String id) {
        return paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }

}
