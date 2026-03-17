package com.projects.edustore.mapper;

import com.projects.edustore.dto.cart.CartResponseDto;
import com.projects.edustore.model.product.Cart;
import com.projects.edustore.model.product.CartItem;

import java.math.BigDecimal;


public class CartMapper {

    private CartMapper() {}

    public static CartResponseDto toCartResponse(Cart cart) {
        CartResponseDto response = new CartResponseDto();
        response.setCartId(cart.getId());

        int totalItemQuantity = 0;
        BigDecimal totalPrice = new BigDecimal("0");

        for(CartItem item : cart.getCartItems()) {
            totalItemQuantity += item.getQuantity();
            response.addItemDto(CartItemMapper.toItemResponse(item));
            totalPrice = totalPrice.add(item.getSubTotal());
        }

        response.setTotalPrice(totalPrice);
        response.setTotalItemAmount(totalItemQuantity);

        return response;
    }

}
