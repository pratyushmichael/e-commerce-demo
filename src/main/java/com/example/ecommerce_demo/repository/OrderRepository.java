package com.example.ecommerce_demo.repository;

import com.example.ecommerce_demo.model.Order;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory repository for completed orders.
 * Tracks a simple global order counter via AtomicLong (used to determine nth order).
 */
@Repository
public class OrderRepository {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong orderCounter = new AtomicLong(0);

    public Order save(Order order) {
        orders.put(order.getOrderId(), order);
        orderCounter.incrementAndGet();
        return order;
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public long getOrderCount() {
        return orderCounter.get();
    }
}

