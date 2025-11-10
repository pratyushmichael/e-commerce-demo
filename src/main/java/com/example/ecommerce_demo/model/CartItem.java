package com.example.ecommerce_demo.model;

import java.math.BigDecimal;

/**
 * Represents an item inside a cart, including quantity and a snapshot
 * of the price at the moment it was added.
 *
 * We store price here so order totals are stable even if item price later changes.
 */
public class CartItem {
    private String itemId;
    private String name;
    private int quantity;
    private BigDecimal unitPrice; // snapshot of price when added

    public CartItem() {}

    public CartItem(String itemId, String name, int quantity, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    /**
     * method to compute subtotal for this cart item.
     */
    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

