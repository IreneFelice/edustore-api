package com.projects.edustore.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class CartItemRequestDto {


    @NotNull(message = "Product Id is required")
    private Long productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



}
