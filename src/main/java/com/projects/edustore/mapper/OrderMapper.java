package com.projects.edustore.mapper;

import com.projects.edustore.dto.order.OrderBasicResponseDto;
import com.projects.edustore.dto.order.OrderDetailsResponseDto;
import com.projects.edustore.dto.order.OrderItemDto;
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
        response.setOrderStatus(order.getOrderStatus());

        return response;
    }

    public static OrderDetailsResponseDto toCustomerResponse(Order order) {
        OrderDetailsResponseDto response = new OrderDetailsResponseDto();

        response.setOrderId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setTotalPrice(order.getTotalPrice());
        response.setOrderStatus(order.getOrderStatus());

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
