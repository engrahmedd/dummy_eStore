package edu.abdullah.final_project.utils;

import java.math.BigDecimal;

public class PaymentRequest {

    private String cardholderName;
    private String cardNumber;
    private String expirationMonth;
    private String expirationYear;
    private String cvv;
    private BigDecimal amount;

    public PaymentRequest() {}

    public PaymentRequest(String cardholderName, String cardNumber,
                          String expirationMonth, String expirationYear,
                          String cvv, BigDecimal amount) {
        this.cardholderName = cardholderName;
        this.cardNumber = cardNumber;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.cvv = cvv;
        this.amount = amount;
    }

    public String getCardholderName() { return cardholderName; }
    public void setCardholderName(String cardholderName) { this.cardholderName = cardholderName; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getExpirationMonth() { return expirationMonth; }
    public void setExpirationMonth(String expirationMonth) { this.expirationMonth = expirationMonth; }

    public String getExpirationYear() { return expirationYear; }
    public void setExpirationYear(String expirationYear) { this.expirationYear = expirationYear; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}