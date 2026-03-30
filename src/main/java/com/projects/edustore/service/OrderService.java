package com.projects.edustore.service;

import com.projects.edustore.dto.order.OrderBasicResponseDto;
import com.projects.edustore.dto.order.OrderDetailsResponseDto;
import com.projects.edustore.dto.order.OrderStudentRequestDto;
import com.projects.edustore.dto.order.OrderStudentResponseDto;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.OrderMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.product.Cart;
import com.projects.edustore.model.product.Order;
import com.projects.edustore.model.product.OrderStatus;
import com.projects.edustore.repository.CartRepository;
import com.projects.edustore.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final CartRepository cartRepos;
    private final OrderRepository orderRepos;
    private final WhoCanSeeWhoService whoCanSee;

    public OrderService(CartRepository cartRepos, OrderRepository orderRepos, WhoCanSeeWhoService whoCanSee) {
        this.cartRepos = cartRepos;
        this.orderRepos = orderRepos;
        this.whoCanSee = whoCanSee;
    }

    public OrderDetailsResponseDto cartToOrder(Long customerId) {
        checkCustomerPermission(customerId);

        Cart cart = cartRepos.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart for customer", customerId));

        Order newOrder = OrderMapper.toEntity(cart);

        orderRepos.save(newOrder);
        cartRepos.delete(cart);
        return OrderMapper.toCustomerResponse(newOrder);
    }

    public OrderDetailsResponseDto getOrderById(Long customerId, Long orderId) {
        checkCustomerPermission(customerId);
        Order order = findOrder(orderId);
        return OrderMapper.toCustomerResponse(order);
    }

    public List<OrderBasicResponseDto> getOrderOverviewByCustomer(Long customerId) {
        checkCustomerPermission(customerId);
        List<Order> orderList = orderRepos.findByCustomerId(customerId);
        return getDtoList(orderList);
    }

    public List<OrderBasicResponseDto> getAllOrders() {
        List<Order> orderList = orderRepos.findAll();
        return getDtoList(orderList);
    }

    public List<OrderBasicResponseDto> getOrdersByStatus(String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());

        List<Order> orderList = orderRepos.findAllByStatus(orderStatus);
        return getDtoList(orderList);
    }

    public OrderStudentResponseDto getStudentOrderById(Long orderId) {
        Order order = findOrder(orderId);
        return OrderMapper.toStudentResponse(order);
    }

    public OrderBasicResponseDto updateStatus(Long orderId, OrderStudentRequestDto dto) {
        Order order = findOrder(orderId);

        order.setStatus(dto.getStatus());

        orderRepos.save(order);
        return OrderMapper.toBasicResponse(order);
    }

    //helpers

    private Order findOrder(Long orderId) {
        Order order = orderRepos.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        return order;
    }

    private List<OrderBasicResponseDto> getDtoList(List<Order> orderList) {
        List<OrderBasicResponseDto> dtoList = new ArrayList<>();

        for(Order order : orderList) {
            dtoList.add(OrderMapper.toBasicResponse(order));
        }
        return dtoList;
    }

    private void checkCustomerPermission(Long customerId) {
        whoCanSee.checkUserPermission(customerId, Role.ROLE_CUSTOMER, "Customer");
    }

}