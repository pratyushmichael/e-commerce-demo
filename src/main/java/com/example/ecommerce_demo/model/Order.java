package com.example.ecommerce_demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents a completed order
 * We include fields for tracking discount usage and amounts
 */
public class Order {
    private String orderId;               // generated UUID
    private String userId;
    private List<CartItem> items;         // snapshot of cart items at time of order
    private BigDecimal totalAmount;       // pre-discount total
    private String appliedDiscountCode;   // code applied 
    private BigDecimal discountAmount;    // absolute amount discounted
    private BigDecimal finalAmount;       // totalAmount - discountAmount
    private LocalDateTime createdAt;

    public Order() {}

    public Order(String userId, List<CartItem> items, BigDecimal totalAmount,
                 String appliedDiscountCode, BigDecimal discountAmount, BigDecimal finalAmount) {
        this.orderId = UUID.randomUUID().toString();
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.appliedDiscountCode = appliedDiscountCode;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.createdAt = LocalDateTime.now();
    }

    // getters 
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public List<CartItem> getItems() { return items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getAppliedDiscountCode() { return appliedDiscountCode; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public BigDecimal getFinalAmount() { return finalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
