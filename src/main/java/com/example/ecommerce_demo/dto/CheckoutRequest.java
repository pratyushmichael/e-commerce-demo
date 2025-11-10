package com.example.ecommerce_demo.dto;

/**
 * Request payload for checkout.
 * couponCode is optional.
 */
public class CheckoutRequest {
    private String couponCode;

    public CheckoutRequest() {}

    public CheckoutRequest(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
}
