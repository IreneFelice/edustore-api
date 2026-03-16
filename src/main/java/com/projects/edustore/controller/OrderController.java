package com.projects.edustore.controller;

import com.projects.edustore.dto.order.OrderBasicResponseDto;
import com.projects.edustore.dto.order.OrderDetailsResponseDto;
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

/* customer sets cart to order
  - customer id --> for finding right cart
  - no dto needed for request
  - later a request dto can contain notes, or discount codes etc.
 */
    @PostMapping("/customer/{customerId}")
    public ResponseEntity<OrderDetailsResponseDto> cartToOrder(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.cartToOrder(customerId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderBasicResponseDto>> getCustomerOrderOverview(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrderOverviewByCustomer(customerId));
    }
/* Customer requests overview of all own orders
    Response dto contains list with per order:
    - id
    - status order
    - date of placement
    (- date of latest status change)
 */

/* Customer requests to see order-details
    customer- and order id in Path-variable
    Response dto contains:
    - items [list response dtos single item]
    - total price
    - id
    - status order
    - date of placement
    - date of latest status change
 */

/* Student/admin wants overview all orders of all customers (per status)
    Path-variable status
 */


/* Student/admin wants order details

 */


/* Student/admin wants to update order status

 */

/* admin wants to delete order

 */

}
