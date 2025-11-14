package com.projects.edustore.controller;

import com.projects.edustore.dto.profileDto.CustomerUserRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;
import com.projects.edustore.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController (CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerUserResponseDto> getCustomerById(
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerUserResponseDto> createCustomerUser(
            @RequestBody CustomerUserRequestDto customerUserRequestDto) {
        return ResponseEntity.ok(customerService.createUser(customerUserRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerUserResponseDto> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerUserRequestDto dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long id) {
        customerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
