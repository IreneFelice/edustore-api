package com.projects.edustore.service;


import com.projects.edustore.dto.cart.CartItemDeleteDto;
import com.projects.edustore.dto.cart.CartItemRequestDto;
import com.projects.edustore.dto.cart.CartItemResponseDto;
import com.projects.edustore.dto.cart.CartResponseDto;
import com.projects.edustore.exception.OutOfStockException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.CartItemMapper;
import com.projects.edustore.mapper.CartMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.CustomerProfile;
import com.projects.edustore.model.product.Cart;
import com.projects.edustore.model.product.CartItem;
import com.projects.edustore.model.product.Product;
import com.projects.edustore.repository.CartItemRepository;
import com.projects.edustore.repository.CartRepository;
import com.projects.edustore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CartService {
    private final ProductRepository productRepos;
    private final CartItemRepository cartItemRepos;
    private final CartRepository cartRepos;
    private final WhoCanSeeWhoService whoCanSee;


    public CartService(ProductRepository productRepos, CartItemRepository cartItemRepos, CartRepository cartRepos, WhoCanSeeWhoService whoCanSee) {
        this.productRepos = productRepos;
        this.cartItemRepos = cartItemRepos;
        this.cartRepos = cartRepos;
        this.whoCanSee = whoCanSee;
    }


    public CartResponseDto getCart(Long id) {
        whoCanSee.checkUserPermission(id, Role.ROLE_CUSTOMER, "Customer");

        Cart existingCart = cartRepos
                .findByCustomerId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart for customer", id));

        return CartMapper.toCartResponse(existingCart);
    }


    @Transactional
    public CartItemResponseDto addItemToCart(Long id, CartItemRequestDto dto) {
        CustomerProfile customer = authorizeAndGetCustomer(id);

        Long productId = dto.getProductId();

        Product product = findProduct(productId);

        Cart cart = getOrCreateCart(customer, id);


        CartItem existingCartItem = cartItemRepos
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (existingCartItem != null) {

            adjustStockByDifference(existingCartItem.getQuantity(), dto.getQuantity(), productId);

            existingCartItem.setQuantity(dto.getQuantity());
            cart.addCartItem(existingCartItem);
            cartRepos.save(cart);
            return CartItemMapper.toItemResponse(existingCartItem);

        } else {
            checkAndAdjustStock(productId, dto.getQuantity());

            CartItem newItem = CartItemMapper.toEntity(product, dto.getQuantity());
            cart.addCartItem(newItem);
            cartRepos.save(cart);
            return CartItemMapper.toItemResponse(newItem);
        }
    }


    @Transactional
    public void deleteItem(Long id, CartItemDeleteDto dto) {
        CustomerProfile customer = authorizeAndGetCustomer(id);
        Cart cart = getOrCreateCart(customer, id);

        Long productId = dto.getProductId();
        CartItem existingCartItem = cartItemRepos
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("In this cart, the product", productId));
        int deletedQuantity = existingCartItem.getQuantity();
        giveBackToStock(productId, deletedQuantity);

        cart.removeCartItem(existingCartItem);
        if (cart.getCartItems().isEmpty()) {
            cartRepos.delete(cart);
        } else {
            cartRepos.save(cart);
        }
    }

    private Cart getOrCreateCart(CustomerProfile customer, Long id) {
        return cartRepos.findByCustomerId(id)
                .orElseGet(() ->
                        createNewCart(customer));
    }

    private Cart createNewCart(CustomerProfile customer) {
        Cart newCart = new Cart(customer);
        cartRepos.save(newCart);
        return newCart;
    }


    //Product

    private Product findProduct(Long productId) {
        return productRepos
                .findById(productId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    //Stock

    private void checkAndAdjustStock(Long productId, int quantity) {
        Product product = findProduct(productId);
        if (product.getStockQuantity() >= quantity) {
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepos.save(product);
        } else {
            throw new OutOfStockException("Product stock is insufficient");
        }
    }

    private void giveBackToStock(Long productId, int quantity) {
        Product product = findProduct(productId);
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepos.save(product);
    }

    private void adjustStockByDifference(int oldQuantity, int newQuantity, Long productId) {
        int quantDiff = oldQuantity - newQuantity;

        if (quantDiff < 0) { //new is more than old, --> check Stock
            int extraQuantity = Math.abs(quantDiff);  // convert negative difference to positive value
            checkAndAdjustStock(productId, extraQuantity);
        } else if (quantDiff > 0) {
            giveBackToStock(productId, quantDiff);
        }
    }

    //Authorization
    private CustomerProfile authorizeAndGetCustomer(Long id) {
        User user = whoCanSee.findUserAndCheckPermission(id, Role.ROLE_CUSTOMER, "Customer");
        return user.getPerson().getCustomerProfile();
    }

}
