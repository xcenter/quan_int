package com.herendi.quan_int.controller;

import com.herendi.quan_int.controller.dto.PaymentRequest;
import com.herendi.quan_int.controller.dto.PaymentResponse;
import com.herendi.quan_int.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payments")
    public PaymentResponse createPayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        return PaymentResponse.from(paymentService.createPayment(
                paymentRequest.amount(),
                paymentRequest.currency(),
                paymentRequest.creditorAccount(),
                paymentRequest.debtorAccount()
        ));
    }

    @GetMapping("/payments/{id}")
    public PaymentResponse getPaymentById(@PathVariable @NotNull String id) {
        return PaymentResponse.from(paymentService.getPaymentById(id));
    }

    @PutMapping("/payments/{id}")
    public PaymentResponse updatePayment(@PathVariable @NotNull String id, @RequestBody @Valid PaymentRequest paymentRequest) {
        return PaymentResponse.from(paymentService.updatePayment(id, paymentRequest));
    }

    @DeleteMapping("/payments/{id}")
    public void deletePayment(@PathVariable @NotNull String id) {
        paymentService.deletePayment(id);
    }

    @GetMapping("/payments")
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments().stream().map(PaymentResponse::from).toList();
    }

    @PostMapping("/payments/{id}/complete")
    public PaymentResponse completePayment(@PathVariable @NotNull String id) {
        return PaymentResponse.from(paymentService.completePayment(id));
    }

    @PostMapping("/payments/{id}/fail")
    public PaymentResponse failPayment(@PathVariable @NotNull String id) {
        return PaymentResponse.from(paymentService.failPayment(id));
    }

}
