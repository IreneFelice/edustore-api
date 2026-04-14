package com.projects.edustore.dto.cart;

import jakarta.validation.constraints.NotNull;


public class CartItemDeleteDto {

    @NotNull(message = "ProductId is required")
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
