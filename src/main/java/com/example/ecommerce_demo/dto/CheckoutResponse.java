package com.example.ecommerce_demo.dto;

import java.math.BigDecimal;

/**
 * Response returned to the client after a successful checkout.
 */
public class CheckoutResponse {
    private String orderId;
    private String userId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String appliedDiscountCode;
    private String generatedDiscountCode; // optional: returned when this checkout triggered nth-order generation

    public CheckoutResponse() {}

    public CheckoutResponse(String orderId, String userId, BigDecimal totalAmount,
                            BigDecimal discountAmount, BigDecimal finalAmount,
                            String appliedDiscountCode, String generatedDiscountCode) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.appliedDiscountCode = appliedDiscountCode;
        this.generatedDiscountCode = generatedDiscountCode;
    }

    // getters & setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public String getAppliedDiscountCode() { return appliedDiscountCode; }
    public void setAppliedDiscountCode(String appliedDiscountCode) { this.appliedDiscountCode = appliedDiscountCode; }

    public String getGeneratedDiscountCode() { return generatedDiscountCode; }
    public void setGeneratedDiscountCode(String generatedDiscountCode) { this.generatedDiscountCode = generatedDiscountCode; }
}
