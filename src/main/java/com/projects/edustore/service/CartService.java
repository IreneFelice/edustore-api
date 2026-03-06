package com.projects.edustore.service;


import com.projects.edustore.dto.cart.CartItemRequestDto;
import com.projects.edustore.dto.cart.CartItemResponseDto;
import com.projects.edustore.dto.cart.CartResponseDto;
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


    private void checkAndAdjustStock(Product product, int quantity) {

        if (product.getStockQuantity() >= quantity) {
            product.setStockQuantity(product.getStockQuantity() - quantity);
        } else {
            throw new RuntimeException(); //TODO: appropriate exception (OutOfStockException)
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

    private void adjustStockByDifference(int oldQuantity, int newQuantity, Product product) {
        int quantDiff = oldQuantity - newQuantity;

        if (quantDiff < 0) { //new is more than old, --> check Stock
            int extraQuantity = Math.abs(quantDiff);  // convert negative difference to positive value
            checkAndAdjustStock(product, extraQuantity);
            productRepos.save(product);
        } else if (quantDiff > 0) {
            product.setStockQuantity(product.getStockQuantity() + quantDiff); // give back to Stock
            productRepos.save(product);
        }
    }

    @Transactional
    public CartItemResponseDto addItemToCart(Long id, CartItemRequestDto dto) {
        User user = whoCanSee.findUserAndCheckPermission(id, Role.ROLE_CUSTOMER, "Customer");
        CustomerProfile customer = user.getPerson().getCustomerProfile();

        Long productId = dto.getProductId();

        Product product = productRepos
                .findById(productId)
                .orElseThrow(ResourceNotFoundException::new);


        Cart cart = getOrCreateCart(customer, id);


        CartItem existingCartItem = cartItemRepos
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (existingCartItem != null) {

            adjustStockByDifference(existingCartItem.getQuantity(), dto.getQuantity(), product);

            existingCartItem.setQuantity(dto.getQuantity());
            cart.addCartItem(existingCartItem);
            cartRepos.save(cart);
            return CartItemMapper.toItemResponse(existingCartItem);

        } else {
            checkAndAdjustStock(product, dto.getQuantity());

            CartItem newItem = CartItemMapper.toEntity(product, dto.getQuantity());
            cart.addCartItem(newItem);
            cartRepos.save(cart);
            return CartItemMapper.toItemResponse(newItem);
        }
    }

    private Cart findCart(Long customerId) {
        return cartRepos.findByCustomerId(customerId).orElseThrow(() -> new ResourceNotFoundException("Cart for customer", customerId));
    }

    public CartResponseDto getCart(Long id) {
        whoCanSee.checkUserPermission(id, Role.ROLE_CUSTOMER, "Customer");

        Cart existingCart = findCart(id);

        return CartMapper.toCartResponse(existingCart);
    }

}
