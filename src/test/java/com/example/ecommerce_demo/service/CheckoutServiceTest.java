package com.example.ecommerce_demo.service;

import com.example.ecommerce_demo.dto.CheckoutRequest;
import com.example.ecommerce_demo.dto.CheckoutResponse;
import com.example.ecommerce_demo.model.Cart;
import com.example.ecommerce_demo.model.CartItem;
import com.example.ecommerce_demo.model.DiscountCode;
import com.example.ecommerce_demo.model.Item;
import com.example.ecommerce_demo.repository.CartRepository;
import com.example.ecommerce_demo.repository.DiscountRepository;
import com.example.ecommerce_demo.repository.ItemRepository;
import com.example.ecommerce_demo.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class CheckoutServiceTest {

    private ItemRepository itemRepository;
    private CartRepository cartRepository;
    private DiscountRepository discountRepository;
    private OrderRepository orderRepository;
    private AdminService adminService;
    private CheckoutService checkoutService;

    @BeforeEach
    void setup() {
        // use real in-memory repositories to test actual behavior
        itemRepository = new ItemRepository();
        // ensure @PostConstruct init runs (seeds items)
        itemRepository.init();

        cartRepository = new CartRepository();
        discountRepository = new DiscountRepository();
        orderRepository = new OrderRepository();
        // adminService configured with high everyNth so it won't auto-generate coupons in test
        adminService = new AdminService(discountRepository, orderRepository, 1000L);

        checkoutService = new CheckoutService(cartRepository, discountRepository, orderRepository, adminService);
    }

    @Test
    void checkout_appliesDiscount_whenValidCouponProvided() {
        String userId = "user-100";

        // create and save a cart with one item
        Cart cart = cartRepository.createIfAbsent(userId);
        cart.addItem(new CartItem("item-1", "Wireless Mouse", 2, new BigDecimal("499.00")));
        cartRepository.save(userId, cart);

        // create a discount code and save
        DiscountCode code = new DiscountCode("TEST10", 10, 0);
        discountRepository.save(code);

        // checkout with coupon
        CheckoutRequest req = new CheckoutRequest("TEST10");
        CheckoutResponse resp = checkoutService.checkout(userId, req);

        assertThat(resp).isNotNull();
        assertThat(resp.getTotalAmount()).isEqualByComparingTo(new BigDecimal("998.00"));
        assertThat(resp.getDiscountAmount()).isEqualByComparingTo(new BigDecimal("99.80"));
        assertThat(resp.getFinalAmount()).isEqualByComparingTo(new BigDecimal("898.20"));
        assertThat(resp.getAppliedDiscountCode()).isEqualTo("TEST10");

        // ensure coupon is marked used
        assertThat(discountRepository.findByCode("TEST10").get().isUsed()).isTrue();

        // ensure order count incremented
        assertThat(orderRepository.getOrderCount()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void checkout_withoutCoupon_createsOrder_noDiscount() {
        String userId = "user-101";
        Cart cart = cartRepository.createIfAbsent(userId);
        cart.addItem(new CartItem("item-2", "Another", 1, new BigDecimal("50.00")));
        cartRepository.save(userId, cart);

        CheckoutResponse resp = checkoutService.checkout(userId, new CheckoutRequest(null));

        assertThat(resp.getDiscountAmount()).isZero();
        assertThat(resp.getFinalAmount()).isEqualByComparingTo(resp.getTotalAmount());
    }
}
