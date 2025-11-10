package com.example.ecommerce_demo.service;

import com.example.ecommerce_demo.dto.CheckoutRequest;
import com.example.ecommerce_demo.dto.CheckoutResponse;
import com.example.ecommerce_demo.model.Cart;
import com.example.ecommerce_demo.model.DiscountCode;
import com.example.ecommerce_demo.model.Order;
import com.example.ecommerce_demo.repository.CartRepository;
import com.example.ecommerce_demo.repository.DiscountRepository;
import com.example.ecommerce_demo.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * CheckoutService performs the checkout logic:
 * - load cart
 * - compute totals
 * - validate and consume discount code (atomically, via DiscountRepository)
 * - persist order and increment order counter (OrderRepository)
 * - trigger admin coupon generation if needed
 *
 * Concurrency notes:
 * - We synchronize on the cart instance when reading/clearing it (since cart mutations may be concurrent).
 * - DiscountRepository exposes an atomic mark-as-used operation to safely consume coupons.
 */
@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final DiscountRepository discountRepository;
    private final OrderRepository orderRepository;
    private final AdminService adminService;

    public CheckoutService(CartRepository cartRepository,
                           DiscountRepository discountRepository,
                           OrderRepository orderRepository,
                           AdminService adminService) {
        this.cartRepository = cartRepository;
        this.discountRepository = discountRepository;
        this.orderRepository = orderRepository;
        this.adminService = adminService;
    }

    /**
     * Performs checkout for a user's cart.
     *
     * @param userId the user performing checkout
     * @param req optional checkout request (may contain coupon code)
     * @return CheckoutResponse with order details and possibly generated coupon
     */
    public CheckoutResponse checkout(String userId, CheckoutRequest req) {
        // load cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart not found or empty"));

        // ensure cart has items
        List<?> itemsSnapshot;
        BigDecimal totalAmount;
        synchronized (cart) {
            if (cart.getItems() == null || cart.getItems().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
            }
            // create snapshot of items and total while synchronized to prevent concurrent updates
            itemsSnapshot = List.copyOf(cart.getItems());
            totalAmount = cart.getTotal();
        }

        BigDecimal discountAmount = BigDecimal.ZERO;
        String appliedCode = null;

        // If coupon provided, validate and consume it atomically
        if (req != null && req.getCouponCode() != null && !req.getCouponCode().isBlank()) {
            String code = req.getCouponCode().trim();
            Optional<DiscountCode> maybe = discountRepository.findByCode(code);

            DiscountCode discountCode = maybe.orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid coupon code"));

            // Try to atomically mark coupon as used via DiscountRepository (method implemented below).
            boolean consumed = discountRepository.markAsUsedIfAvailable(code);

            if (!consumed) {
                // someone else already used it
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon code already used");
            }

            // compute discount amount
            discountAmount = totalAmount.multiply(BigDecimal.valueOf(discountCode.getDiscountPercent()))
                    .divide(BigDecimal.valueOf(100));
            appliedCode = code;
        }

        BigDecimal finalAmount = totalAmount.subtract(discountAmount);

        // create and persist order (Order constructor uses UUID and createdAt)
        Order order = new Order(userId, (List) itemsSnapshot, totalAmount, appliedCode, discountAmount, finalAmount);
        orderRepository.save(order);

        // after saving, get the order count and check coupon generation
        long orderCount = orderRepository.getOrderCount();
        String generatedCoupon = adminService.generateDiscountIfNeeded(orderCount);

        // clear user's cart
        cartRepository.delete(userId);

        // build response
        CheckoutResponse resp = new CheckoutResponse(
                order.getOrderId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getDiscountAmount(),
                order.getFinalAmount(),
                order.getAppliedDiscountCode(),
                generatedCoupon
        );

        return resp;
    }
}

