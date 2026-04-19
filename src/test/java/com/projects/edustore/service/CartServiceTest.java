package com.projects.edustore.service;

import com.projects.edustore.dto.cart.CartItemDeleteDto;
import com.projects.edustore.dto.cart.CartItemRequestDto;
import com.projects.edustore.dto.cart.CartItemResponseDto;
import com.projects.edustore.dto.cart.CartResponseDto;
import com.projects.edustore.exception.OutOfStockException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.CustomerProfile;
import com.projects.edustore.model.person.Person;
import com.projects.edustore.model.product.Cart;
import com.projects.edustore.model.product.CartItem;
import com.projects.edustore.model.product.Product;
import com.projects.edustore.repository.CartItemRepository;
import com.projects.edustore.repository.CartRepository;
import com.projects.edustore.repository.ProductRepository;
import com.projects.edustore.security.AuthorisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    AuthorisationService authorizer;

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
        cart.setId(1L);
        when(cartRepos.findByCustomerId(userId)).thenReturn(Optional.of(cart));
        return cart;
    }

    private CartItem arrangeExistingItem(Product product) {
        CartItem existingItem = new CartItem();
        existingItem.setId(1L);
        existingItem.setProduct(product);
        return existingItem;
    }

    private CartItemRequestDto arrangeRequestDto() {
        CartItemRequestDto dto = new CartItemRequestDto();
        dto.setProductId(1L);
        dto.setQuantity(2);
        System.out.println("Requested quantity: " + dto.getQuantity());
        return dto;
    }

    private void assertItemToCart(CartItemResponseDto result, Product product, Cart cart) {
        assertEquals(2, result.getQuantity());
        assertEquals(1, result.getProductId());
        assertEquals(8, product.getStockQuantity());
        assertEquals(1, cart.getCartItems().size());
        assertEquals(2, cart.getCartItems().get(0).getQuantity());
        System.out.println("ProductName: " + result.getProductName());
        System.out.println("Current item quantity: " + result.getQuantity());
        System.out.println("New stock: " + product.getStockQuantity());
        System.out.println("//");
    }

    @Test
    void getCart() {
        System.out.println("Test getCart:");
        //arrange
        doNothing().when(authorizer).checkSelfOrAdminAccess(userId);
        arrangeCart();

        //act
        CartResponseDto result = cartService.getCart(userId);

        //assert
        assertNotNull(result);
        assertNotNull(result.getItems());
        assertEquals(1, result.getCartId());
        System.out.println("Cart id: " + result.getCartId());
        System.out.println("//");
    }

    @Test
    void getCartNotFound() {
        System.out.println("Test getCart_NotFound:");
        //arrange
        doNothing().when(authorizer).checkSelfOrAdminAccess(userId);

        //act and assert
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class, () -> cartService.getCart(userId)
        );

        assertEquals("Cart for customer with identifier 1 could not be found.", ex.getMessage());
        System.out.println("Exception thrown: " + ex.getMessage());
        System.out.println("//");
    }

    @Test
    void addNewItemToCart() {
        System.out.println("Test addNewItemToCart:");
        //arrange
        when(authorizer.findUserAndCheckAuthorisation(userId)).thenReturn(Optional.of(user));
        Product product = arrangeProduct();
        Cart cart = arrangeCart();
        CartItemRequestDto dto = arrangeRequestDto();

        System.out.println("Old stock: " + product.getStockQuantity());

        when(cartItemRepos.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.empty());

        //act
        CartItemResponseDto result = cartService.addItemToCart(userId, dto);

        //assert
        assertItemToCart(result, product, cart);
        verify(productRepos).save(product);
    }

    @Test
    void addItemToNewCart() {
        System.out.println("Test addItemToNewCart:");
        //arrange
        when(authorizer.findUserAndCheckAuthorisation(userId)).thenReturn(Optional.of(user));
        Product product = arrangeProduct();
        //no cart arranging
        CartItemRequestDto dto = arrangeRequestDto();

        System.out.println("old stock: " + product.getStockQuantity());

        when(cartItemRepos.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.empty());

        //act
        CartItemResponseDto result = cartService.addItemToCart(userId, dto);

        //assert
        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepos).save(cartCaptor.capture());

        Cart savedCart = cartCaptor.getValue();

        assertItemToCart(result, product, savedCart);
    }

    @Test
    void addExistingItemToCart() {
        System.out.println("Test addExistingItemToCart:");
        //arrange
        when(authorizer.findUserAndCheckAuthorisation(userId)).thenReturn(Optional.of(user));

        Product product = arrangeProduct();
        Cart cart = arrangeCart();
        CartItem existingItem = arrangeExistingItem(product);
        existingItem.setQuantity(1);

        cart.addCartItem(existingItem);
        product.setStockQuantity(product.getStockQuantity() - existingItem.getQuantity());

        CartItemRequestDto dto = arrangeRequestDto();

        System.out.println("old item quantity: " + existingItem.getQuantity());
        System.out.println("old stock: " + product.getStockQuantity());
        when(cartItemRepos.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.of(existingItem));

        //act
        CartItemResponseDto result = cartService.addItemToCart(userId, dto);

        //assert
        assertItemToCart(result, product, cart);
    }

    @Test
    void addExistingItemToCartGiveBackToStock() {
        System.out.println("Test addExistingItemToNewCart_GiveBackToStock:");
        //arrange
        when(authorizer.findUserAndCheckAuthorisation(userId)).thenReturn(Optional.of(user));

        Product product = arrangeProduct();
        Cart cart = arrangeCart();
        CartItem existingItem = arrangeExistingItem(product);
        existingItem.setQuantity(4);

        cart.addCartItem(existingItem);
        product.setStockQuantity(product.getStockQuantity() - existingItem.getQuantity());

        CartItemRequestDto dto = arrangeRequestDto();

        System.out.println("old item quantity: " + existingItem.getQuantity());
        System.out.println("old stock: " + product.getStockQuantity());
        when(cartItemRepos.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.of(existingItem));

        //act
        CartItemResponseDto result = cartService.addItemToCart(userId, dto);

        //assert
        assertItemToCart(result, product, cart);
    }

    @Test
    void addExistingItemToCartOutOfStock() {
        System.out.println("Test addExistingItemToNewCart_OutOfStock:");
        //arrange
        when(authorizer.findUserAndCheckAuthorisation(userId)).thenReturn(Optional.of(user));

        Product product = arrangeProduct();
        Cart cart = arrangeCart();
        CartItem existingItem = arrangeExistingItem(product);
        existingItem.setQuantity(4);

        cart.addCartItem(existingItem);
        product.setStockQuantity(product.getStockQuantity() - existingItem.getQuantity());

        CartItemRequestDto dto = new CartItemRequestDto();
        dto.setProductId(1L);
        dto.setQuantity(12);
        System.out.println("Requested quantity: " + dto.getQuantity());

        System.out.println("old item quantity: " + existingItem.getQuantity());
        System.out.println("old stock: " + product.getStockQuantity());
        when(cartItemRepos.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.of(existingItem));

        //act and assert
        OutOfStockException ex = assertThrows(
                OutOfStockException.class, () -> cartService.addItemToCart(userId, dto)
        );

        assertEquals("Product stock is insufficient", ex.getMessage());
        System.out.println("Exception thrown: " + ex.getMessage());
        System.out.println("Current item quantity: " + existingItem.getQuantity());
        System.out.println("New stock: " + product.getStockQuantity());
        System.out.println("//");
    }

    @Test
    void deleteItemNotCart() {
        System.out.println("Test delete item, not cart:");
        //arrange
        when(authorizer.findUserAndCheckAuthorisation(userId)).thenReturn(Optional.of(user));
        Cart cart = arrangeCart();
        Product product1 = arrangeProduct();

        Product product2 = arrangeProduct();
        product2.setId(2L);
        product2.setName("testProduct2");

        CartItem existingItem1 = arrangeExistingItem(product1);
        existingItem1.setQuantity(4);

        CartItem existingItem2 = arrangeExistingItem(product2);
        existingItem2.setId(2L);
        existingItem2.setQuantity(1);

        cart.addCartItem(existingItem1);
        cart.addCartItem(existingItem2);

        product1.setStockQuantity(product1.getStockQuantity() - existingItem1.getQuantity());
        product2.setStockQuantity(product2.getStockQuantity() - existingItem2.getQuantity());


        CartItemDeleteDto dto = new CartItemDeleteDto();
        dto.setProductId(product1.getId());

        System.out.println("Number of unique items in cart, before delete: " + cart.getCartItems().size());

        when(cartItemRepos.findByCartIdAndProductId(cart.getId(), product1.getId()))
                .thenReturn(Optional.of(existingItem1));

        //act
        cartService.deleteItem(userId, dto);

        //assert
        assertEquals(1, cart.getCartItems().size());
        verify(cartRepos).save(cart);
        System.out.println("Number of unique items in cart after delete: " + cart.getCartItems().size());
        System.out.println("//");
    }

    @Test
    void deleteItemAndCart() {
        System.out.println("Test delete item AND cart:");
        //arrange
        when(authorizer.findUserAndCheckAuthorisation(userId)).thenReturn(Optional.of(user));
        Cart cart = arrangeCart();
        Product product1 = arrangeProduct();

        CartItem existingItem1 = arrangeExistingItem(product1);
        existingItem1.setQuantity(4);
        cart.addCartItem(existingItem1);
        product1.setStockQuantity(product1.getStockQuantity() - existingItem1.getQuantity());

        CartItemDeleteDto dto = new CartItemDeleteDto();
        dto.setProductId(product1.getId());

        System.out.println("Number of unique items in cart, before delete: " + cart.getCartItems().size());

        when(cartItemRepos.findByCartIdAndProductId(cart.getId(), product1.getId()))
                .thenReturn(Optional.of(existingItem1));

        //act
        cartService.deleteItem(userId, dto);

        //assert
        assertEquals(0, cart.getCartItems().size());
        verify(cartRepos).delete(cart);
        System.out.println("Number of unique items in cart after delete: " + cart.getCartItems().size());
        System.out.println("//");
    }

}