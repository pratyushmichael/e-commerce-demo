package com.example.ecommerce_demo.service;

import com.example.ecommerce_demo.dto.AdminSummary;
import com.example.ecommerce_demo.model.CartItem;
import com.example.ecommerce_demo.model.DiscountCode;
import com.example.ecommerce_demo.model.Order;
import com.example.ecommerce_demo.repository.DiscountRepository;
import com.example.ecommerce_demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Admin related operations, including:
 *  - coupon generation (every Nth order)
 *  - summarizing orders and discounts
 *
 * This service uses the in-memory repositories for the exercise.
 */
@Service
public class AdminService {

    private final DiscountRepository discountRepository;
    private final OrderRepository orderRepository;

    // configurable nth order; default 5 if not provided
    private final long everyNth;

    public AdminService(DiscountRepository discountRepository,
                        OrderRepository orderRepository,
                        @Value("${ecommerce.coupon.everyNth:5}") long everyNth) {
        this.discountRepository = discountRepository;
        this.orderRepository = orderRepository;
        this.everyNth = everyNth;
    }

    /**
     * If the provided orderCount meets the everyNth rule, generate a new DiscountCode
     * and persist it. Returns the generated code string or null if none created.
     */
    public String generateDiscountIfNeeded(long orderCount) {
        if (everyNth <= 0) return null;
        if (orderCount % everyNth != 0) return null;

        // create a unique code (human readable-ish)
        String code = "SAVE10-" + UUID.randomUUID().toString().substring(0, 8);
        DiscountCode discount = new DiscountCode(code, 10, orderCount);
        discountRepository.save(discount);
        return code;
    }

    /**
     * Produce a summary of purchases and discounts:
     * - total items purchased (sum of quantities across all orders)
     * - total purchase amount (sum of order.totalAmount)
     * - list of discount codes (from repo)
     * - total discount amount (sum of order.discountAmount)
     */
    public AdminSummary getSummary() {
        // gather orders
        List<Order> orders = orderRepository.findAll();

        // compute total items purchased
        long totalItems = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .mapToLong(i -> {
                    if (i instanceof CartItem) {
                        return ((CartItem) i).getQuantity();
                    } else {
                        // If the stored item type changes, default to 0
                        return 0L;
                    }
                })
                .sum();

        // total purchase amount (pre-discount)
        BigDecimal totalPurchase = orders.stream()
                .map(Order::getTotalAmount)
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // total discount amount given
        BigDecimal totalDiscount = orders.stream()
                .map(Order::getDiscountAmount)
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // get list of discount codes
        List<DiscountCode> codes = discountRepository.findAll();

        return new AdminSummary(totalItems, totalPurchase, codes, totalDiscount);
    }
}
