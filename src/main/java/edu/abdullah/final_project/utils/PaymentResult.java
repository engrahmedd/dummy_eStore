package edu.abdullah.final_project.utils;

import edu.abdullah.final_project.enums.PaymentStatus;

public class PaymentResult {

    private final PaymentStatus status;
    private final String message;
    private final String transactionId;

    public PaymentResult(PaymentStatus status, String message, String transactionId) {
        this.status = status;
        this.message = message;
        this.transactionId = transactionId;
    }

    public boolean isApproved() {
        return status == PaymentStatus.APPROVED;
    }

    public PaymentStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public String getTransactionId() { return transactionId; }
}