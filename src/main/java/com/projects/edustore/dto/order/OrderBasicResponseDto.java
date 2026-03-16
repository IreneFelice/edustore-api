package com.projects.edustore.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projects.edustore.model.product.OrderStatus;

import java.time.LocalDateTime;

public class OrderBasicResponseDto {
    Long OrderId;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    LocalDateTime orderDate;

    OrderStatus orderStatus;

    public Long getOrderId() {
        return OrderId;
    }

    public void setOrderId(Long orderId) {
        OrderId = orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
