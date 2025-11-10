package com.example.ecommerce_demo.repository;
import com.example.ecommerce_demo.model.DiscountCode;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for DiscountCode objects.
 * The map is keyed by discount code string.
 */
@Repository
public class DiscountRepository {

    private final Map<String, DiscountCode> codes = new ConcurrentHashMap<>();

    public void save(DiscountCode discount) {
        codes.put(discount.getCode(), discount);
    }

    public Optional<DiscountCode> findByCode(String code) {
        return Optional.ofNullable(codes.get(code));
    }

    public List<DiscountCode> findAll() {
        return new ArrayList<>(codes.values());
    }

     /**
     * Atomically mark the discount as used if it was not used before.
     * Returns true if successful (we consumed it), false if it was already used or not found.
     */
    public boolean markAsUsedIfAvailable(String code) {
        DiscountCode d = codes.get(code);
        if (d == null) return false;
        synchronized (d) {
            if (d.isUsed()) return false;
            d.setUsed(true);
            return true;
        }
    }
}

