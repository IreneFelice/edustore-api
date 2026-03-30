package com.projects.edustore.service;

import com.projects.edustore.dto.order.OrderBasicResponseDto;
import com.projects.edustore.dto.order.OrderDetailsResponseDto;
import com.projects.edustore.dto.order.OrderStudentRequestDto;
import com.projects.edustore.dto.order.OrderStudentResponseDto;
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
    private WhoCanSeeWhoService whoCanSee;

    @InjectMocks
    private OrderService orderService;

    private CustomerProfile customer;
    private User user;
    private Long userId;
    private Cart cart;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User("TestName", "password", Role.ROLE_CUSTOMER);
        Person person = Person.create(user, "firstName", "lastName", "email@email.com");
        customer = CustomerProfile.create(person, "0612345678");
        userId = 1L;

        // Mock product
        Product product = new Product();
        product.setId(10L);
        product.setName("Asbak van klei");
        product.setPrice(new BigDecimal("5"));

        // CartItem
        CartItem cartItem = new CartItem(product, 2); // quantity = 2

        // Cart
        cart = new Cart(customer);
        cart.addCartItem(cartItem);

        //Order
        order = OrderMapper.toEntity(cart);
    }

    @Test
    void cartToOrder() {
        when(cartRepos.findByCustomerId(userId)).thenReturn(Optional.of(cart));

        Order savedOrder = OrderMapper.toEntity(cart);

        when(orderRepos.save(any(Order.class))).thenReturn(savedOrder);

        OrderDetailsResponseDto result = orderService.cartToOrder(userId);

        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(1, result.getItems().size());
        assertEquals("Asbak van klei", result.getItems().get(0).getProductName());

        verify(cartRepos).delete(cart);
        verify(orderRepos).save(any(Order.class));
    }

    @Test
    void getOrderById() {
        when(orderRepos.findById(order.getId())).thenReturn(Optional.of(order));

        OrderDetailsResponseDto result = orderService.getOrderById(userId, order.getId());

        assertNotNull(result);
        assertEquals(order.getStatus(), result.getStatus());
        assertEquals(1, result.getItems().size());
    }

    @Test
    void getOrderOverviewByCustomer() {
        when(orderRepos.findByCustomerId(userId)).thenReturn(List.of(order));

        List<OrderBasicResponseDto> result = orderService.getOrderOverviewByCustomer(1L);

        assertEquals(1, result.size());
        assertEquals(order.getTotalPrice(), result.get(0).getTotalPrice());
    }

    @Test
    void getAllOrders() {
        when(orderRepos.findAll()).thenReturn(List.of(order));

        List<OrderBasicResponseDto> result = orderService.getAllOrders();

        assertEquals(1, result.size());
    }

    @Test
    void getOrdersByStatus() {
        when(orderRepos.findAllByStatus(OrderStatus.PENDING))
                .thenReturn(List.of(order));

        List<OrderBasicResponseDto> result = orderService.getOrdersByStatus("pending");

        assertEquals(1, result.size());
        assertEquals(OrderStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    void getStudentOrderById() {
        when(orderRepos.findById(order.getId())).thenReturn(Optional.of(order));

        OrderStudentResponseDto result = orderService.getStudentOrderById(order.getId());

        assertEquals("lastName", result.getCustomerName());
        assertEquals("email@email.com", result.getEmail());
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