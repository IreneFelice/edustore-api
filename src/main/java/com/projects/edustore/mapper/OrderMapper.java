package com.projects.edustore.mapper;

import com.projects.edustore.dto.order.*;
import com.projects.edustore.model.product.Cart;
import com.projects.edustore.model.product.CartItem;
import com.projects.edustore.model.product.Order;
import com.projects.edustore.model.product.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class OrderMapper {

    public static Order toEntity(Cart cart) {
        Order order = new Order(cart.getCustomer());

        BigDecimal totalPrice = new BigDecimal("0");

        for (CartItem item : cart.getCartItems()) {

            OrderItem orderItem = new OrderItem(
                    item.getProduct(),
                    item.getQuantity()
            );
            order.addItem(orderItem);
            totalPrice = totalPrice.add(orderItem.getSubtotal());
        }

        order.setTotalPrice(totalPrice);

        return order;
    }

    public static OrderBasicResponseDto toBasicResponse(Order order) {
        OrderBasicResponseDto response = new OrderBasicResponseDto();

        response.setOrderId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setOrderStatus(order.getStatus());
        response.setOrderTotal(order.getTotalPrice());

        return response;
    }

    public static OrderDetailsResponseDto toCustomerResponse(Order order) {
        OrderDetailsResponseDto response = new OrderDetailsResponseDto();

        response.setOrderId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());

        List<OrderItemDto> dtoItems = response.getItems(); // Empty ArrayList, to add OrderItemDto

        for (OrderItem item : order.getItems()) {

            OrderItemDto dto = new OrderItemDto();

            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setPrice(item.getProduct().getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setSubtotal(item.getSubtotal());

            dtoItems.add(dto);
        }

        response.addItemDtos(dtoItems);

        return response;
    }

    public static OrderStudentResponseDto toStudentResponse(Order order) {
        OrderStudentResponseDto response = new OrderStudentResponseDto();

        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getPerson().getLastName());
        response.setEmail(order.getCustomer().getPerson().getEmail());

        response.setOrderId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());

        List<OrderItemDto> dtoItems = response.getItems(); // Empty ArrayList, to add OrderItemDto

        for (OrderItem item : order.getItems()) {

            OrderItemDto dto = new OrderItemDto();

            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setPrice(item.getProduct().getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setSubtotal(item.getSubtotal());

            dtoItems.add(dto);
        }

        response.addItemDtos(dtoItems);

        return response;
    }


}

/*
    private Long customerId;
    private String CustomerName;
    private String email;
 */