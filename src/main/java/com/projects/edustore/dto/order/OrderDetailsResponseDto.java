package com.projects.edustore.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projects.edustore.model.product.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsResponseDto {
    private Long orderId;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime orderDate;

    private OrderStatus orderStatus;
    private List<OrderItemDto> items = new ArrayList<>();
    private BigDecimal totalPrice;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {


        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void addItemDtos(List<OrderItemDto> items) {
        this.items = items;
    }
}
