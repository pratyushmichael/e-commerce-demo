package com.example.ecommerce_demo.model;

import java.math.BigDecimal;

/**
 * Represents a product/item sold in the store.

 */

public class Item {
    private String id;               // unique identifier for the item => SKUs
    private String name;             
    private String description;      
    private BigDecimal price;        // unit price 
    public Item() {}

    public Item(String id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

