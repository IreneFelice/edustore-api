package com.projects.edustore.controller;

import com.projects.edustore.dto.order.OrderBasicResponseDto;
import com.projects.edustore.dto.order.OrderDetailsResponseDto;
import com.projects.edustore.dto.order.OrderStudentRequestDto;
import com.projects.edustore.model.product.OrderStatus;
import com.projects.edustore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<OrderDetailsResponseDto> cartToOrder() {
        OrderDetailsResponseDto response = orderService.cartToOrder();

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/orders/{id}")
                .buildAndExpand(response.getOrderId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderBasicResponseDto>> getOrders(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) OrderStatus status) {
    return ResponseEntity.ok(orderService.getOrders(customerId, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity <OrderDetailsResponseDto> getOrderDetails(
            @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderDetails(id));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/{id}")
    public ResponseEntity<OrderBasicResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestBody OrderStudentRequestDto orderStudentRequestDto) {
    return ResponseEntity.ok(orderService.updateStatus(id, orderStudentRequestDto));
    }

}
