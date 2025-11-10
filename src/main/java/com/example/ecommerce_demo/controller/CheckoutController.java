package com.example.ecommerce_demo.controller;

import com.example.ecommerce_demo.dto.CheckoutRequest;
import com.example.ecommerce_demo.dto.CheckoutResponse;
import com.example.ecommerce_demo.service.CheckoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Exposes checkout endpoint:
 * - POST /cart/{userId}/checkout
 */
@RestController
@RequestMapping("/cart")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<CheckoutResponse> checkout(
            @PathVariable String userId,
            @RequestBody(required = false) CheckoutRequest request) {
        CheckoutResponse resp = checkoutService.checkout(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}

