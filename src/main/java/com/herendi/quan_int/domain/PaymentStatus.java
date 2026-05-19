package com.herendi.quan_int.domain;

public enum PaymentStatus {
    CREATED("CREATED"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    public final String statusName;

    PaymentStatus(String statusName) {
        this.statusName = statusName;
    }
}
