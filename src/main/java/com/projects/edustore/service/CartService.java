package com.projects.edustore.service;

import com.projects.edustore.dto.cart.CartItemDeleteDto;
import com.projects.edustore.dto.cart.CartItemRequestDto;
import com.projects.edustore.dto.cart.CartItemResponseDto;
import com.projects.edustore.dto.cart.CartResponseDto;
import com.projects.edustore.exception.OutOfStockException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.CartItemMapper;
import com.projects.edustore.mapper.CartMapper;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.CustomerProfile;
import com.projects.edustore.model.product.Cart;
import com.projects.edustore.model.product.CartItem;
import com.projects.edustore.model.product.Product;
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
            //item already exists in cart; replace old quantity by new
            //stock adjustment based on difference between the two quantities
            adjustStockByDifference(existingCartItem.getQuantity(), dto.getQuantity(), productId);
            existingCartItem.setQuantity(dto.getQuantity());
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