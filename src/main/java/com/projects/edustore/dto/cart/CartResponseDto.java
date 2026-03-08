package com.projects.edustore.dto.cart;

import java.util.ArrayList;
import java.util.List;

public class CartResponseDto {

    private Long cartId;

    private List<CartItemResponseDto> items = new ArrayList<>();

    private int totalItemAmount;


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

    public int getTotalItemAmount() {
        return totalItemAmount;
    }

    public void setTotalItemAmount(int totalItemAmount) {
        this.totalItemAmount = totalItemAmount;
    }
}
