package com.projects.edustore.service;


import com.projects.edustore.dto.OrderDto.CartRequestDto;
import com.projects.edustore.dto.OrderDto.CartResponseDto;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.CartItemMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.products.CartItem;
import com.projects.edustore.model.products.Product;
import com.projects.edustore.repository.CartItemRepository;
import com.projects.edustore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartItemService {
    private final ProductRepository repos;
    private final CartItemRepository cartRepos;
    private final WhoCanSeeWhoService whoCanSee;


    public CartItemService(ProductRepository repos, CartItemRepository cartRepos, WhoCanSeeWhoService whoCanSee) {
        this.repos = repos;
        this.cartRepos = cartRepos;
        this.whoCanSee = whoCanSee;
    }

    public CartResponseDto addItemToCart(Long id, CartRequestDto dto) {
        User customer = whoCanSee.authorizeUserAccess(id, Role.ROLE_CUSTOMER, "Customer");
        Product product = repos.findById(dto.getProductId()).orElseThrow(ResourceNotFoundException::new);

        //is stock not empty
        if (product.getStockQuantity() >= dto.getQuantity()) {

            //TODO: check if productId is added already, if not:
            CartItem newCartItem = CartItemMapper.toEntity(product, dto.getQuantity());

            //adjust stock
            product.setStockQuantity(product.getStockQuantity() - dto.getQuantity());

            newCartItem.setCustomer(customer.getPerson().getCustomerProfile());
            cartRepos.save(newCartItem);

            //TODO: if productId is added already, update quantity cartItem

            return CartItemMapper.toResponse(newCartItem);
        } else {
            throw new RuntimeException(); //TODO: appropriate exception
        }
    }

    public List<CartResponseDto> getCartItems(Long id) {
        whoCanSee.authorizeUserAccess(id, Role.ROLE_CUSTOMER, "Customer");

        List<CartResponseDto> cartItems = new ArrayList<>();
        List<CartItem> items = cartRepos.findByCustomerId(id);

        for (CartItem item : items) {
            cartItems.add(CartItemMapper.toResponse(item));
        }

        return cartItems;
    }

}
