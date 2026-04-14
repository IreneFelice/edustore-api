package com.projects.edustore.mapper;

import com.projects.edustore.dto.order.OrderItemDto;
import com.projects.edustore.model.product.OrderItem;

public class OrderItemMapper {

    public static OrderItemDto toItemDto(OrderItem item) {
        OrderItemDto response = new OrderItemDto();

        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setPrice(item.getProduct().getPrice());
        response.setQuantity(item.getQuantity());
        response.setSubtotal(item.getSubtotal());

        return response;
    }
}
