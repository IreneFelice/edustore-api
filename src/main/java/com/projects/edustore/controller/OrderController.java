package com.projects.edustore.controller;

import com.projects.edustore.dto.order.OrderBasicResponseDto;
import com.projects.edustore.dto.order.OrderDetailsResponseDto;
import com.projects.edustore.dto.order.OrderStudentRequestDto;
import com.projects.edustore.dto.order.OrderStudentResponseDto;
import com.projects.edustore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/customer/{customerId}")
    public ResponseEntity<OrderDetailsResponseDto> cartToOrder(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.cartToOrder(customerId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderBasicResponseDto>> getOrderOverviewForCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrderOverviewByCustomer(customerId));
    }

    @GetMapping("/customer/{customerId}/details/{orderId}")
    public ResponseEntity<OrderDetailsResponseDto> getOrderByIdForCustomer(
            @PathVariable Long customerId,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderByIdForCustomer(customerId, orderId));
    }

    @GetMapping("/student")
    public ResponseEntity<List<OrderBasicResponseDto>> getAllOrdersForStudent() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    @GetMapping("/student/details/{orderId}")
    public ResponseEntity<OrderStudentResponseDto> getOrderByIdForStudent(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderByIdForStudent(orderId));
    }

    @GetMapping("/student/status/{statusName}")
    public ResponseEntity<List<OrderBasicResponseDto>> getOrdersByStatus(
            @PathVariable String statusName) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(statusName));
    }

    @PatchMapping("/student/status/{orderId}")
    public ResponseEntity<OrderBasicResponseDto> updateStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStudentRequestDto orderStudentRequestDto) {
    return ResponseEntity.ok(orderService.updateStatus(orderId, orderStudentRequestDto));
    }


}
