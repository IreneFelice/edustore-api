package com.projects.edustore.service;

import com.projects.edustore.dto.cart.CartItemResponseDto;
import com.projects.edustore.dto.cart.CartDetailsResponseDto;
import com.projects.edustore.dto.cart.CartResponseDto;
import com.projects.edustore.exception.OutOfStockException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.product.journey.CartItemMapper;
import com.projects.edustore.mapper.product.journey.CartMapper;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.CustomerProfile;
import com.projects.edustore.model.product.journey.Cart;
import com.projects.edustore.model.product.journey.CartItem;
import com.projects.edustore.model.product.journey.Product;
import com.projects.edustore.repository.CartItemRepository;
import com.projects.edustore.repository.CartRepository;
import com.projects.edustore.repository.ProductRepository;
import com.projects.edustore.security.AuthorisationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CartService {
    private final ProductRepository productRepos;
    private final CartItemRepository cartItemRepos;
    private final CartRepository cartRepos;
    private final AuthorisationService authorizer;


    public CartService(ProductRepository productRepos, CartItemRepository cartItemRepos, CartRepository cartRepos, AuthorisationService authorizer) {
        this.productRepos = productRepos;
        this.cartItemRepos = cartItemRepos;
        this.cartRepos = cartRepos;
        this.authorizer = authorizer;
    }

    public CartResponseDto getCart(Long id) {
        authorizer.checkSelfOrAdminAccess(id);

        Cart existingCart = cartRepos
                .findByCustomerId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart for customer", id));

        return CartMapper.toCartResponse(existingCart);

    }

    public CartDetailsResponseDto getCartDetails(Long id) {
        authorizer.checkSelfOrAdminAccess(id);

        Cart existingCart = cartRepos
                .findByCustomerId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart for customer", id));

        return CartMapper.toCartDetailsResponse(existingCart);
    }

    @Transactional
    public CartItemResponseDto setCartItemQuantity(Long customerId, Long productId, int newQuantity) {
        CustomerProfile customer = authorizeAndGetCustomer(customerId);
        Product product = findProduct(productId);
        Cart cart = getOrCreateCart(customer, customerId);

        CartItem existingCartItem = cartItemRepos
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (existingCartItem != null) {
            //item already exists in cart; replace old quantity by new
            //stock adjustment based on difference between the two quantities
            adjustStockByDifference(existingCartItem.getQuantity(), newQuantity, productId);

            existingCartItem.setQuantity(newQuantity);
            cartRepos.save(cart);
            return CartItemMapper.toItemResponse(existingCartItem);
        } else {
            checkAndAdjustStock(productId, newQuantity);
            CartItem newItem = CartItemMapper.toEntity(product, newQuantity);
            cart.addCartItem(newItem);
            cartRepos.save(cart);
            return CartItemMapper.toItemResponse(newItem);
        }
    }

    @Transactional
    public void deleteItem(Long id, Long productId) {
        CustomerProfile customer = authorizeAndGetCustomer(id);
        Cart cart = getOrCreateCart(customer, id);

        CartItem existingCartItem = cartItemRepos
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("In this cart, the product", productId));
        int deletedQuantity = existingCartItem.getQuantity();
        giveBackToStock(productId, deletedQuantity);
        cart.removeCartItem(existingCartItem);
        if (cart.getCartItems().isEmpty()) {
            cartRepos.delete(cart);
            customer.setCart(null);
        } else {
            cartRepos.save(cart);
        }
    }

    private Cart getOrCreateCart(CustomerProfile customer, Long id) {
        return cartRepos.findByCustomerId(id)
                .orElseGet(() ->
                        new Cart(customer));
    }

    private Product findProduct(Long productId) {
        return productRepos
                .findById(productId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    private void checkAndAdjustStock(Long productId, int quantity) {
        Product product = findProduct(productId);
        if (product.getStockQuantity() >= quantity) {
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepos.save(product);
        } else {
            throw new OutOfStockException();
        }
    }

    private void giveBackToStock(Long productId, int quantity) {
        Product product = findProduct(productId);
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepos.save(product);
    }


    private void adjustStockByDifference(int oldQuantity, int newQuantity, Long productId) {
        int quantDiff = newQuantity - oldQuantity;
        if (oldQuantity < newQuantity) { //extra quantity is requested
            checkAndAdjustStock(productId, quantDiff);
        } else if (newQuantity < oldQuantity) { //quantDiff is negative
            int surplusQuantity = Math.abs(quantDiff); //convert negative difference to positive value
            giveBackToStock(productId, surplusQuantity);
        }
    }

    private CustomerProfile authorizeAndGetCustomer(Long id) {
        User user = authorizer.findUserAndCheckAuthorisation(id).orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        return user.getPerson().getCustomerProfile();
    }
}