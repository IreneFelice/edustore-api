package com.projects.edustore.mapper.product.journey;

import com.projects.edustore.dto.order.*;
import com.projects.edustore.mapper.product.journey.OrderItemMapper;
import com.projects.edustore.model.product.journey.Cart;
import com.projects.edustore.model.product.journey.CartItem;
import com.projects.edustore.model.product.journey.Order;
import com.projects.edustore.model.product.journey.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class OrderMapper {

    public static Order toEntity(Cart cart) {
        Order order = new Order(cart.getCustomer());

        BigDecimal totalPrice = BigDecimal.ZERO;

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

    private static void mapBasicFields(Order order, OrderBasicResponseDto dto) {
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
    }

    private static void mapOrderItems(Order order, OrderDetailsResponseDto dto) {
        List<OrderItemDto> dtoItems = dto.getItems(); // Empty ArrayList, to add OrderItemDto

        for (OrderItem item : order.getItems()) {
            dtoItems.add(OrderItemMapper.toItemDto(item));
        }

        dto.addItemDtos(dtoItems);
    }

    public static OrderBasicResponseDto toBasicResponse(Order order) {
        OrderBasicResponseDto dto = new OrderBasicResponseDto();
        mapBasicFields(order, dto);

        return dto;
    }

    public static OrderDetailsResponseDto toCustomerResponse(Order order) {
        OrderDetailsResponseDto dto = new OrderDetailsResponseDto();
        mapBasicFields(order, dto);
        mapOrderItems(order, dto);

        return dto;
    }

    public static OrderStudentResponseDto toStudentResponse(Order order) {
        OrderStudentResponseDto dto = new OrderStudentResponseDto();
        mapBasicFields(order, dto);
        mapOrderItems(order, dto);

        dto.setCustomerId(order.getCustomer().getId());
        dto.setCustomerName(order.getCustomer().getPerson().getLastName());
        dto.setEmail(order.getCustomer().getPerson().getEmail());

        return dto;
    }

}
