package com.projects.edustore.dto.order;

import com.projects.edustore.model.product.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class OrderStudentRequestDto {

    @NotNull(message = "Status is required. Allowed values: PENDING, READY, CLOSED, CANCELED")
    OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
