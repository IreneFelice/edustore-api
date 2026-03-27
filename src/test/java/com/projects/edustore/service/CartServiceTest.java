package com.projects.edustore.service;

import com.projects.edustore.dto.cart.CartItemRequestDto;
import com.projects.edustore.dto.cart.CartItemResponseDto;
import com.projects.edustore.dto.cart.CartResponseDto;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.CustomerProfile;
import com.projects.edustore.model.person.Person;
import com.projects.edustore.model.product.Cart;
import com.projects.edustore.model.product.Product;
import com.projects.edustore.repository.CartItemRepository;
import com.projects.edustore.repository.CartRepository;
import com.projects.edustore.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    private CustomerProfile profile;
    private User user;
    private Long userId;
    private Long productId;
    private Long cartId;

    @Mock
    CartRepository cartRepos;

    @Mock
    CartItemRepository cartItemRepos;

    @Mock
    ProductRepository productRepos;

    @Mock
    WhoCanSeeWhoService whoCanSee;

    @InjectMocks
    CartService cartService;

    @BeforeEach
    void setup() {
        user = new User("TestName", "password", Role.ROLE_CUSTOMER);
        Person person = Person.create(user, "firstName", "lastName", "email@email.com");
        profile = CustomerProfile.create(person, "0612345678");
        userId = 1L;
    }

    private Product arrangeProduct() {
        Product product = new Product();
        productId = 1L;
        product.setId(productId);
        product.setName("testProduct");
        product.setPrice(BigDecimal.valueOf(1.00));
        product.setStockQuantity(10);
        when(productRepos.findById(productId)).thenReturn(Optional.of(product));

        return product;
    }

    private Cart arrangeCart() {
        Cart cart = new Cart(profile);
        cartId = 1L;
        cart.setId(cartId);
        when(cartRepos.findByCustomerId(userId)).thenReturn(Optional.of(cart));

        return cart;
    }


    @Test
    void getCart() {
        //arrange
        doNothing().when(whoCanSee).checkUserPermission(userId, Role.ROLE_CUSTOMER, "Customer");
        arrangeCart();

        //act
        CartResponseDto result = cartService.getCart(userId);

        //assert
        assertNotNull(result);
    }


    @Test
    void addItemToCart() {
        //arrange
        when(whoCanSee.findUserAndCheckPermission(userId, Role.ROLE_CUSTOMER, "Customer")).thenReturn(user);
        Product product = arrangeProduct();
        Cart cart = arrangeCart();

        CartItemRequestDto dto = new CartItemRequestDto();
        dto.setProductId(1L);
        dto.setQuantity(2);

        when(cartItemRepos.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.empty());

        //act
        CartItemResponseDto result = cartService.addItemToCart(userId, dto);

        //assert
        assertEquals(2, result.getQuantity());
        assertEquals(1, result.getProductId());
        assertEquals(8, product.getStockQuantity());
        assertNotNull(cart);
        assertNotNull(cart.getCartItems());
        System.out.println("quantity: " + result.getQuantity());
        System.out.println("productName: " + result.getProductName());
        System.out.println("new stock: " + product.getStockQuantity());
    }

}