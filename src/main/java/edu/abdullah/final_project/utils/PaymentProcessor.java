package edu.abdullah.final_project.utils;

public interface PaymentProcessor {
    PaymentResult processPayment(PaymentRequest request);
}
