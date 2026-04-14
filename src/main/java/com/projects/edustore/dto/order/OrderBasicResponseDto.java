package com.projects.edustore.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projects.edustore.model.product.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderBasicResponseDto {
    private Long orderId;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime orderDate;

    private OrderStatus status;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
