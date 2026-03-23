package com.projects.edustore.mapper;

import com.projects.edustore.dto.cart.CartItemResponseDto;
import com.projects.edustore.model.product.CartItem;
import com.projects.edustore.model.product.Product;

public class CartItemMapper {

    public static CartItem toEntity(Product product, int quantity) {

        return new CartItem(product, quantity);
    }

    public static CartItemResponseDto toItemResponse(CartItem item) {
        CartItemResponseDto response = new CartItemResponseDto();

        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setSubTotal(item.getSubTotal());

        return response;
    }

}
