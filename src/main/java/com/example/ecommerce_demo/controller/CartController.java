package com.example.ecommerce_demo.controller;

import com.example.ecommerce_demo.dto.AddToCartRequest;
import com.example.ecommerce_demo.model.Cart;
import com.example.ecommerce_demo.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing cart-related endpoints.
 *
 * Endpoints implemented:
 * - POST /cart/{userId}/items  -> add item to cart
 * - GET  /cart/{userId}        -> retrieve cart
 *
 * This controller focuses on small, focused endpoints to keep behavior predictable.
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Add an item to a user's cart.
     * Returns the updated cart.
     */
    @PostMapping("/{userId}/items")
    public ResponseEntity<Cart> addItem(
            @PathVariable String userId,
            @Valid @RequestBody AddToCartRequest request) {

        Cart updated = cartService.addItemToCart(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    /**
     * Get the user's current cart. If none exists, an empty cart is returned.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }
}

