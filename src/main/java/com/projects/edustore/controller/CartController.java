package com.projects.edustore.controller;

import com.projects.edustore.dto.cart.*;
import com.projects.edustore.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers/{id}/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCart(id));
    }

    @GetMapping("/items")
    public ResponseEntity<CartDetailsResponseDto> getCartDetails(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartDetails(id));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartItemResponseDto> setCartItemQuantity(
            @PathVariable Long id,
            @PathVariable Long productId,
            @Valid @RequestBody CartItemRequestDto dto) {
        return ResponseEntity.ok(cartService.setCartItemQuantity(id, productId, dto.getQuantity()));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id,
            @PathVariable Long productId) {
        cartService.deleteItem(id, productId);
        return ResponseEntity.noContent().build();
    }

}
