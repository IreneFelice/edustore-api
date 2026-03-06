package com.projects.edustore.controller;

import com.projects.edustore.dto.cart.CartItemRequestDto;
import com.projects.edustore.dto.cart.CartItemResponseDto;
import com.projects.edustore.dto.cart.CartResponseDto;
import com.projects.edustore.dto.profile.CustomerUserRequestDto;
import com.projects.edustore.dto.profile.CustomerUserResponseDto;
import com.projects.edustore.service.CartService;
import com.projects.edustore.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    public CartService cartService;

    public CustomerController (CustomerService customerService, CartService cartService){
        this.customerService = customerService;
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerUserResponseDto> getCustomerById(
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerUserResponseDto> createCustomerUser(
            @Valid @RequestBody CustomerUserRequestDto customerUserRequestDto) {
        return ResponseEntity.ok(customerService.createUser(customerUserRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerUserResponseDto> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUserRequestDto dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long id) {
        customerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}/cart")
    public ResponseEntity<CartItemResponseDto> addItemToCart(
            @PathVariable Long id,
            @Valid @RequestBody CartItemRequestDto dto) {
        return ResponseEntity.ok(cartService.addItemToCart(id, dto));
    }

    @GetMapping("/{id}/cart")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCart(id));
    }



    //TODO: delete cartItem

    //TODO: create order

    //TODO: getOrder


}
