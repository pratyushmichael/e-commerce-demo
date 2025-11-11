package com.example.ecommerce_demo.service;

import com.example.ecommerce_demo.dto.AddToCartRequest;
import com.example.ecommerce_demo.model.Cart;
import com.example.ecommerce_demo.model.Item;
import com.example.ecommerce_demo.repository.CartRepository;
import com.example.ecommerce_demo.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addItemToCart_createsCartAndAddsItem_whenItemExists() {
        String userId = "user-1";
        String itemId = "item-1";

        Item item = new Item(itemId, "Test Item", "desc", new BigDecimal("100.00"));

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        // createIfAbsent should return a new Cart
        when(cartRepository.createIfAbsent(userId)).thenReturn(new Cart(userId));

        AddToCartRequest req = new AddToCartRequest(itemId, 2);
        Cart cart = cartService.addItemToCart(userId, req);

        assertThat(cart).isNotNull();
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getItemId()).isEqualTo(itemId);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(2);

        // verify repository save called
        verify(cartRepository, atLeastOnce()).save(eq(userId), any(Cart.class));
    }

    @Test
    void addItemToCart_throwsWhenItemNotFound() {
        String userId = "user-1";
        String itemId = "missing";

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        when(cartRepository.createIfAbsent(userId)).thenReturn(new Cart(userId));

        AddToCartRequest req = new AddToCartRequest(itemId, 1);

        assertThatThrownBy(() -> cartService.addItemToCart(userId, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Item not found");
    }
}
