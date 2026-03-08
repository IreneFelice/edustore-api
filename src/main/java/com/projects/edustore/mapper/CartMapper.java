package com.projects.edustore.mapper;

import com.projects.edustore.dto.cart.CartResponseDto;
import com.projects.edustore.model.product.Cart;
import com.projects.edustore.model.product.CartItem;



public class CartMapper {

    private CartMapper() {}

    public static CartResponseDto toCartResponse(Cart cart) {
        CartResponseDto response = new CartResponseDto();
        response.setCartId(cart.getId());

        int totalItemQuantity = 0;

        for(CartItem item : cart.getCartItems()) {
            totalItemQuantity += item.getQuantity();
            response.addItemDto(CartItemMapper.toItemResponse(item));
        }

        response.setTotalItemAmount(totalItemQuantity);
        return response;
    }
}
