package com.projects.edustore.controller;

import com.projects.edustore.dto.cart.CartItemDeleteDto;
import com.projects.edustore.dto.cart.CartItemRequestDto;
import com.projects.edustore.dto.cart.CartItemResponseDto;
import com.projects.edustore.dto.cart.CartResponseDto;
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


    @PutMapping()
    public ResponseEntity<CartItemResponseDto> addItemToCart(
            @PathVariable Long id,
            @Valid @RequestBody CartItemRequestDto dto) {
        return ResponseEntity.ok(cartService.addItemToCart(id, dto));
    }

    @GetMapping()
    public ResponseEntity<CartResponseDto> getCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCart(id));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id,
            @Valid @RequestBody CartItemDeleteDto dto) {
        cartService.deleteItem(id, dto);
        return ResponseEntity.noContent().build();
    }

}
