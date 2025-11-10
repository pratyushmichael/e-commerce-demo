package com.example.ecommerce_demo.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A user's shopping cart.
 *
 *  We keep the cart simple and in-memory:
 * - userId identifies the owner
 * - items is a list of CartItem 
 * - couponCode is optional and validated at checkout
 */
public class Cart {
    private String userId;
    private List<CartItem> items = new ArrayList<>();
    private String appliedCouponCode; // optional coupon code user set before checkout

    public Cart() {}

    public Cart(String userId) {
        this.userId = userId;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public String getAppliedCouponCode() { return appliedCouponCode; }
    public void setAppliedCouponCode(String appliedCouponCode) { this.appliedCouponCode = appliedCouponCode; }

    /**
     * Compute cart total (sum of item subtotals).
     * Note: discount application is handled during checkout, not here.
     */
    public BigDecimal getTotal() {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Adds an item to the cart. If the item already exists, increase quantity.
     * This keeps idempotency at the cart level.
     */
    public void addItem(CartItem newItem) {
        for (CartItem ci : items) {
            if (ci.getItemId().equals(newItem.getItemId())) {
                ci.setQuantity(ci.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        items.add(newItem);
    }
}

