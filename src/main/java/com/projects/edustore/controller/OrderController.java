package com.projects.edustore.controller;

import com.projects.edustore.dto.order.OrderBasicResponseDto;
import com.projects.edustore.dto.order.OrderDetailsResponseDto;
import com.projects.edustore.dto.order.OrderStudentRequestDto;
import com.projects.edustore.model.product.OrderStatus;
import com.projects.edustore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(orderService.cartToOrder());
    }

    @GetMapping
    public ResponseEntity<List<OrderBasicResponseDto>> getOrders(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) OrderStatus status) {
    return ResponseEntity.ok(orderService.getOrders(customerId, status));
    }

    @GetMapping("/details")
    public ResponseEntity <OrderDetailsResponseDto> getOrderDetails(
            @RequestParam Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderBasicResponseDto> updateStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStudentRequestDto orderStudentRequestDto) {
    return ResponseEntity.ok(orderService.updateStatus(orderId, orderStudentRequestDto));
    }

}
