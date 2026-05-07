package com.projects.edustore.service;

import com.projects.edustore.dto.order.OrderBasicResponseDto;
import com.projects.edustore.dto.order.OrderDetailsResponseDto;
import com.projects.edustore.dto.order.OrderStudentRequestDto;
import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.product.journey.OrderMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.product.journey.Cart;
import com.projects.edustore.model.product.journey.Order;
import com.projects.edustore.model.product.journey.OrderStatus;
import com.projects.edustore.repository.CartRepository;
import com.projects.edustore.repository.OrderRepository;
import com.projects.edustore.security.AuthorisationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final CartRepository cartRepos;
    private final OrderRepository orderRepos;
    private final AuthorisationService authorizer;

    public OrderService(CartRepository cartRepos, OrderRepository orderRepos, AuthorisationService authorizer) {
        this.cartRepos = cartRepos;
        this.orderRepos = orderRepos;
        this.authorizer = authorizer;
    }

    public OrderDetailsResponseDto cartToOrder() {
        User currentUser = authorizer.getCurrentUser();
        Long userId = currentUser.getId();

        Cart cart = cartRepos.findByCustomerId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart for customer", userId));

        Order newOrder = OrderMapper.toEntity(cart);

        orderRepos.save(newOrder);
        cartRepos.delete(cart);
        return OrderMapper.toCustomerResponse(newOrder);
    }


    public List<OrderBasicResponseDto> getOrders(Long customerId, OrderStatus status) {
        User currentUser = authorizer.getCurrentUser();
        List<Order> orders;

        if (currentUser.getRole().equals(Role.ROLE_CUSTOMER)) {

            // for customer
            Long ownId = currentUser.getId();
            if (customerId != null && !customerId.equals(ownId)) {
                throw new ForbiddenActionException();
            }
            orders = orderRepos.findByCustomerId(ownId);
        } else {

            // for student/admin
            if (customerId != null) {
                orders = orderRepos.findByCustomerId(customerId);
            } else {
                orders = orderRepos.findAll();
            }
        }

        // OrderStatus
        if (status != null) {
            List<Order> filteredOrders = new ArrayList<>();

            for (Order order : orders) {
                if (order.getStatus() == (status)) {
                    filteredOrders.add(order);
                }
            }
            return getBasicDtoList(filteredOrders);
        } else {
            return getBasicDtoList(orders);
        }
    }

    public OrderDetailsResponseDto getOrderDetails(Long orderId) {
        Order order = findOrder(orderId);
        User currentUser = authorizer.getCurrentUser();

        if (currentUser.getRole() == Role.ROLE_CUSTOMER) {
            checkCustomerPermission(order.getCustomer().getId());
            return OrderMapper.toCustomerResponse(order);

        } else {
            return OrderMapper.toStudentResponse(order);
        }
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

    private List<OrderBasicResponseDto> getBasicDtoList(List<Order> orderList) {
        List<OrderBasicResponseDto> dtoList = new ArrayList<>();

        for (Order order : orderList) {
            dtoList.add(OrderMapper.toBasicResponse(order));
        }
        return dtoList;
    }

    private void checkCustomerPermission(Long customerId) {
        authorizer.checkSelfOrAdminAccess(customerId);
    }

}