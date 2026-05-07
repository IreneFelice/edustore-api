package com.projects.edustore.mapper.product.journey;

import com.projects.edustore.dto.cart.CartDetailsResponseDto;
import com.projects.edustore.dto.cart.CartResponseDto;
import com.projects.edustore.mapper.product.journey.CartItemMapper;
import com.projects.edustore.model.product.journey.Cart;
import com.projects.edustore.model.product.journey.CartItem;

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
            totalPrice = totalPrice.add(item.getSubTotal());
        }

        response.setTotalPrice(totalPrice);
        response.setTotalItemAmount(totalItemQuantity);

        return response;
    }

    public static CartDetailsResponseDto toCartDetailsResponse(Cart cart) {
        CartDetailsResponseDto response = new CartDetailsResponseDto();
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
