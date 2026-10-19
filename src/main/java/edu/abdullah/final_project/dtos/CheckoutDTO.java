package edu.abdullah.final_project.dtos;

public class CheckoutDTO {

    private String shippingName;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String cardholderName;
    private String cardNumber;
    private String expirationMonth;
    private String expirationYear;
    private String cvv;

    public String getShippingName() { return shippingName; }
    public void setShippingName(String shippingName) { this.shippingName = shippingName; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getShippingCity() { return shippingCity; }
    public void setShippingCity(String shippingCity) { this.shippingCity = shippingCity; }

    public String getShippingState() { return shippingState; }
    public void setShippingState(String shippingState) { this.shippingState = shippingState; }

    public String getShippingZip() { return shippingZip; }
    public void setShippingZip(String shippingZip) { this.shippingZip = shippingZip; }

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
}