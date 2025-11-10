package com.example.ecommerce_demo.repository;
import com.example.ecommerce_demo.model.Item;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory repository for items.
 * In a real app this would be backed by a DB.
 *
 * This repo is read-only and pre-seeded with sample products.
 */
@Repository
public class ItemRepository {

    private final Map<String, Item> items = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // Seed some sample items
        items.put("item-1", new Item("item-1", "Wireless Mouse", "Ergonomic wireless mouse", new BigDecimal("499.00")));
        items.put("item-2", new Item("item-2", "Mechanical Keyboard", "Compact mechanical keyboard", new BigDecimal("2499.00")));
        items.put("item-3", new Item("item-3", "USB-C Cable", "1.5m fast charge cable", new BigDecimal("199.00")));
    }

    public Optional<Item> findById(String id) {
        return Optional.ofNullable(items.get(id));
    }

    public Map<String, Item> findAll() {
        return Collections.unmodifiableMap(items);
    }
}

