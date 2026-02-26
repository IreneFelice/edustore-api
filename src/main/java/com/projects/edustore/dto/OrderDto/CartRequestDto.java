package com.projects.edustore.dto.OrderDto;



public class CartRequestDto {

    private Long productId;

    private int quantity;

    //TODO: item update
//    private Long cartItemId;

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
