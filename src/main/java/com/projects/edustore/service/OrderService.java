package com.projects.edustore.service;

import com.projects.edustore.dto.order.OrderBasicResponseDto;
import com.projects.edustore.dto.order.OrderDetailsResponseDto;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.OrderMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.product.Cart;
import com.projects.edustore.model.product.Order;
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
        whoCanSee.checkUserPermission(customerId, Role.ROLE_CUSTOMER, "Customer");

        Cart cart = cartRepos.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart for customer", customerId));

        Order newOrder = OrderMapper.toEntity(cart);
        //TODO: delete cart!
        orderRepos.save(newOrder);

        return OrderMapper.toCustomerResponse(newOrder);
    }


    public List<OrderBasicResponseDto> getOrderOverviewByCustomer(Long customerId) {

        List<Order> orderList = orderRepos.findByCustomerId(customerId);

        List<OrderBasicResponseDto> dtoList = new ArrayList<>();

        for(Order order : orderList) {
            dtoList.add(OrderMapper.toBasicResponse(order));
        }

        return dtoList;
    }

}