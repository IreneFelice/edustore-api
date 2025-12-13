package com.projects.edustore.mapper;

import com.projects.edustore.dto.OrderDto.CartResponseDto;
import com.projects.edustore.model.products.CartItem;
import com.projects.edustore.model.products.Product;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public static CartItem toEntity(Product product, int quantity) {

        CartItem cartItem = new CartItem(product, quantity);
        return cartItem;
    }

    public static CartResponseDto toResponse(CartItem item) {
        CartResponseDto response = new CartResponseDto();
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());

        return response;
    }

}
