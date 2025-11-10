package com.example.ecommerce_demo.model;

import java.time.LocalDateTime;

/**
 * Represents a discount coupon code that can be applied to an order.
 *
 * - discountPercent is the percentage (e.g., 10 for 10%)
 * - usableOnce indicates whether the code has been consumed (true = already used)
 * - generatedForOrderNumber can optionally track the nth order that generated it.
 */
public class DiscountCode {
    private String code;
    private int discountPercent; // e.g., 10 for 10%
    private boolean used;        // true if already consumed
    private LocalDateTime createdAt;
    private long generatedForOrderNumber; // optional: the global order count when generated

    public DiscountCode() {}

    public DiscountCode(String code, int discountPercent, long generatedForOrderNumber) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.used = false;
        this.createdAt = LocalDateTime.now();
        this.generatedForOrderNumber = generatedForOrderNumber;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public long getGeneratedForOrderNumber() { return generatedForOrderNumber; }
    public void setGeneratedForOrderNumber(long generatedForOrderNumber) { this.generatedForOrderNumber = generatedForOrderNumber; }
}
