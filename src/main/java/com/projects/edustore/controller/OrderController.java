package com.projects.edustore.controller;

import com.projects.edustore.dto.order.OrderCustomerResponseDto;
import com.projects.edustore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/customer/{customerId}")
    public ResponseEntity<OrderCustomerResponseDto> cartToOrder(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.cartToOrder(customerId));
    }


}
