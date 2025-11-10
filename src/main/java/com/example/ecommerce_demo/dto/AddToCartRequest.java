package com.example.ecommerce_demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for adding an item to a user's cart.
 */
public class AddToCartRequest {

    @NotBlank
    private String itemId;

    @Min(1)
    private int quantity = 1;

    public AddToCartRequest() {}

    public AddToCartRequest(String itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

