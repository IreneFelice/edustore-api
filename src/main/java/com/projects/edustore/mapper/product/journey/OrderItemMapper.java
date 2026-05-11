package com.projects.edustore.mapper.product.journey;

import com.projects.edustore.dto.order.OrderItemDto;
import com.projects.edustore.model.product.journey.OrderItem;

public class OrderItemMapper {

    public static OrderItemDto toItemDto(OrderItem item) {
        OrderItemDto response = new OrderItemDto();

        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProductName());
        response.setPrice(item.getProductPrice());
        response.setQuantity(item.getQuantity());
        response.setSubtotal(item.getSubtotal());

        return response;
    }
}
