package com.example.ecommerce_demo.dto;

import com.example.ecommerce_demo.model.DiscountCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO returned by the admin summary endpoint.
 */
public class AdminSummary {
    private long totalItemsPurchased;
    private BigDecimal totalPurchaseAmount;
    private List<DiscountCode> discountCodes;
    private BigDecimal totalDiscountAmount;

    public AdminSummary() {}

    public AdminSummary(long totalItemsPurchased, BigDecimal totalPurchaseAmount,
                        List<DiscountCode> discountCodes, BigDecimal totalDiscountAmount) {
        this.totalItemsPurchased = totalItemsPurchased;
        this.totalPurchaseAmount = totalPurchaseAmount;
        this.discountCodes = discountCodes;
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public long getTotalItemsPurchased() { return totalItemsPurchased; }
    public void setTotalItemsPurchased(long totalItemsPurchased) { this.totalItemsPurchased = totalItemsPurchased; }

    public BigDecimal getTotalPurchaseAmount() { return totalPurchaseAmount; }
    public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount) { this.totalPurchaseAmount = totalPurchaseAmount; }

    public List<DiscountCode> getDiscountCodes() { return discountCodes; }
    public void setDiscountCodes(List<DiscountCode> discountCodes) { this.discountCodes = discountCodes; }

    public BigDecimal getTotalDiscountAmount() { return totalDiscountAmount; }
    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) { this.totalDiscountAmount = totalDiscountAmount; }
}

