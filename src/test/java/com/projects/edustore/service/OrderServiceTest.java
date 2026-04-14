package com.projects.edustore.service;

import com.projects.edustore.dto.order.OrderBasicResponseDto;
import com.projects.edustore.dto.order.OrderDetailsResponseDto;
import com.projects.edustore.dto.order.OrderStudentRequestDto;
import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.mapper.OrderMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.CustomerProfile;
import com.projects.edustore.model.person.Person;
import com.projects.edustore.model.product.*;
import com.projects.edustore.repository.CartRepository;
import com.projects.edustore.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartRepository cartRepos;

    @Mock
    private OrderRepository orderRepos;

    @Mock
    private AuthorisationService whoCanSee;

    @InjectMocks
    private OrderService orderService;

    private User customerUser;
    private User studentUser;
    private Cart cart;
    private Order order;


    @BeforeEach
    void setUp() {
        // customer
        customerUser = new User("TestName", "password", Role.ROLE_CUSTOMER);
        customerUser.setId(1L);
        Person person = Person.create(customerUser, "firstName", "lastName", "customer@email.com");
        CustomerProfile customer = CustomerProfile.create(person, "0612345678");

        // student
        studentUser = new User("Student", "password", Role.ROLE_STUDENT);
        studentUser.setId(2L);

        // product
        Product product = new Product();
        product.setId(10L);
        product.setName("Asbak van klei");
        product.setPrice(new BigDecimal("5"));

        // CartItem
        CartItem cartItem = new CartItem(product, 2);

        // Cart
        cart = new Cart(customer);
        cart.addCartItem(cartItem);

        //Order
        order = OrderMapper.toEntity(cart);
    }

    @Test
    void cartToOrder() {
        when(whoCanSee.getCurrentUser()).thenReturn(customerUser);
        when(cartRepos.findByCustomerId(customerUser.getId())).thenReturn(Optional.of(cart));
        when(orderRepos.save(any(Order.class))).thenReturn(order);

        OrderDetailsResponseDto result = orderService.cartToOrder();

        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(1, result.getItems().size());
        assertEquals("Asbak van klei", result.getItems().get(0).getProductName());

        verify(cartRepos).delete(cart);
        verify(orderRepos).save(any(Order.class));
    }

    @Test
    void getOrders_customer_onlyOwnOrders() {
        when(whoCanSee.getCurrentUser()).thenReturn(customerUser);
        when(orderRepos.findByCustomerId(customerUser.getId()))
                .thenReturn(List.of(order));

        List<OrderBasicResponseDto> result =
                orderService.getOrders(null, null);

        assertEquals(1, result.size());
    }

    @Test
    void getOrders_customer_isNotOwner() {
        when(whoCanSee.getCurrentUser()).thenReturn(customerUser);

        Long strangerId = customerUser.getId() + 9;

        ForbiddenActionException ex = assertThrows(
                ForbiddenActionException.class,
                () -> orderService.getOrders(strangerId, null)
        );

        assertEquals(
                "You are not allowed to access or modify this resource",
                ex.getMessage()
        );
    }
    @Test
    void getOrders_withStatusFilter_forCustomer() {
        when(whoCanSee.getCurrentUser()).thenReturn(customerUser);
        when(orderRepos.findByCustomerId(customerUser.getId()))
                .thenReturn(List.of(order));

        List<OrderBasicResponseDto> result = orderService.getOrders(null, OrderStatus.PENDING);

        assertEquals(1, result.size());
    }

    @Test
    void getOrders_all_forStudent() {
        when(whoCanSee.getCurrentUser()).thenReturn(studentUser);
        when(orderRepos.findAll())
                .thenReturn(List.of(order));

        List<OrderBasicResponseDto> result =
                orderService.getOrders(null, null);

        assertEquals(1, result.size());
    }

    @Test
    void getOrders_withCustomerId_withStatusFilter_forStudent() {
        when(whoCanSee.getCurrentUser()).thenReturn(studentUser);
        when(orderRepos.findByCustomerId(customerUser.getId()))
                .thenReturn(List.of(order));

        List<OrderBasicResponseDto> result = orderService.getOrders(1L, OrderStatus.PENDING);

        assertEquals(1, result.size());
    }

    @Test
    void getOrderDetails_forCustomer() {
        when(whoCanSee.getCurrentUser()).thenReturn(customerUser);
        when(orderRepos.findById(order.getId())).thenReturn(Optional.of(order));

        OrderDetailsResponseDto result = orderService.getOrderDetails(order.getId());

        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals("Asbak van klei", result.getItems().get(0).getProductName());
    }

    @Test
    void getOrderDetails_asStudent() {
        when(whoCanSee.getCurrentUser()).thenReturn(studentUser);
        when(orderRepos.findById(order.getId())).thenReturn(Optional.of(order));

        OrderDetailsResponseDto result = orderService.getOrderDetails(order.getId());

        assertNotNull(result);
    }

    @Test
    void updateStatus() {
        OrderStudentRequestDto dto = new OrderStudentRequestDto();
        dto.setStatus(OrderStatus.CLOSED);

        when(orderRepos.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepos.save(any(Order.class))).thenReturn(order);

        OrderBasicResponseDto result = orderService.updateStatus(order.getId(), dto);

        assertEquals(OrderStatus.CLOSED, result.getStatus());
        verify(orderRepos).save(order);
    }

}