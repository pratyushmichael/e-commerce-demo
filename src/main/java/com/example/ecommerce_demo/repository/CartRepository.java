package com.example.ecommerce_demo.repository;
import com.example.ecommerce_demo.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for storing user carts.
 * Keyed by userId.
 */
@Repository
public class CartRepository {

    private final Map<String, Cart> carts = new ConcurrentHashMap<>();

    public Optional<Cart> findByUserId(String userId) {
        return Optional.ofNullable(carts.get(userId));
    }

    public Cart createIfAbsent(String userId) {
        // atomically create a new cart if none exists
        return carts.computeIfAbsent(userId, Cart::new);
    }

    public void save(String userId, Cart cart) {
        carts.put(userId, cart);
    }

    public void delete(String userId) {
        carts.remove(userId);
    }
}

