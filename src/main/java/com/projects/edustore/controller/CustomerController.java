package com.projects.edustore.controller;

import com.projects.edustore.dto.profile.CustomerUserRequestDto;
import com.projects.edustore.dto.profile.CustomerUserResponseDto;
import com.projects.edustore.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerUserResponseDto> getCustomerById(
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerUserResponseDto> createCustomerUser(
            @Valid @RequestBody CustomerUserRequestDto customerUserRequestDto) {
        CustomerUserResponseDto response = customerService.createUser(customerUserRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/customers/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
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

}
