package com.projects.edustore.dto.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartResponseDto {

    private Long cartId;

    private List<CartItemResponseDto> items = new ArrayList<>();

    private BigDecimal totalPrice;

    private Integer totalItemAmount;


    //getters + setters

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public void addItemDto(CartItemResponseDto itemDto) {
        items.add(itemDto);
    }

    public List<CartItemResponseDto> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalItemAmount() {
        return totalItemAmount;
    }

    public void setTotalItemAmount(int totalItemAmount) {
        this.totalItemAmount = totalItemAmount;
    }

}
