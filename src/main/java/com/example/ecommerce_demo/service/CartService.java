package com.example.ecommerce_demo.service;

import com.example.ecommerce_demo.dto.AddToCartRequest;
import com.example.ecommerce_demo.model.Cart;
import com.example.ecommerce_demo.model.CartItem;
import com.example.ecommerce_demo.model.Item;
import com.example.ecommerce_demo.repository.CartRepository;
import com.example.ecommerce_demo.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service containing cart-related business logic.
 *
 * Responsibilities:
 * - add item(s) to cart (snapshot unit price)
 * - get cart for user
 *
 * Note: cart-level methods are thread-safe where required.
 */
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public CartService(CartRepository cartRepository, ItemRepository itemRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Adds an item to the user's cart. If the cart doesn't exist, it's created.
     * We read the Item from ItemRepository to snapshot the current price & name.
     *
     * @param userId the user id owning the cart
     * @param req add-to-cart request (itemId + quantity)
     * @return the updated Cart
     * @throws IllegalArgumentException if item not found or quantity invalid
     */
    public Cart addItemToCart(String userId, AddToCartRequest req) {
        if (req.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be >= 1");
        }

        Item item = itemRepository.findById(req.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + req.getItemId()));

        // Create cart if missing
        Cart cart = cartRepository.createIfAbsent(userId);

        // Create CartItem snapshot using current item price
        CartItem cartItem = new CartItem(item.getId(), item.getName(), req.getQuantity(), item.getPrice());

        // synchronize on the cart instance to avoid races when multiple requests update same cart
        synchronized (cart) {
            cart.addItem(cartItem);
            // repo save is idempotent because computeIfAbsent already put same cart, but we'll still persist the reference
            cartRepository.save(userId, cart);
        }

        return cart;
    }

    /**
     * Retrieves the cart for the given user. If none exists, returns an empty cart instance.
     */
    public Cart getCart(String userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> new Cart(userId));
    }
}

