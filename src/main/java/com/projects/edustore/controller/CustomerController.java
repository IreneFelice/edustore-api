package com.projects.edustore.controller;

import com.projects.edustore.dto.OrderDto.CartRequestDto;
import com.projects.edustore.dto.OrderDto.CartResponseDto;
import com.projects.edustore.dto.profileDto.CustomerUserRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;
import com.projects.edustore.service.CartItemService;
import com.projects.edustore.service.CustomerService;
import com.projects.edustore.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
//    public ProductService productService;
    public CartItemService cartItemService;

    public CustomerController (CustomerService customerService, ProductService productService, CartItemService cartItemService){
        this.customerService = customerService;
//        this.productService = productService;
        this.cartItemService = cartItemService;
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

    @PostMapping("/{id}/cart")
    public ResponseEntity<CartResponseDto> addItemToCart(@PathVariable Long id, @RequestBody CartRequestDto dto) {
        return ResponseEntity.ok(cartItemService.addItemToCart(id, dto));
    }

    @GetMapping("/{id}/cart")
    public ResponseEntity<List<CartResponseDto>> getCartItems(@PathVariable Long id) {
        return ResponseEntity.ok(cartItemService.getCartItems(id));
    }


    //TODO: update cartItem

    //TODO: delete cartItem

    //TODO: create order

    //TODO: getOrder


}
