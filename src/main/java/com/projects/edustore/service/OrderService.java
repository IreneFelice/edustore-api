package com.projects.edustore.service;

import com.projects.edustore.dto.order.OrderCustomerResponseDto;
import com.projects.edustore.repository.CartRepository;
import com.projects.edustore.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
private final CartRepository cartRepos;
private final OrderRepository orderRepos;

    public OrderService(CartRepository cartRepos, OrderRepository orderRepos) {
        this.cartRepos = cartRepos;
        this.orderRepos = orderRepos;
    }

    public OrderCustomerResponseDto cartToOrder(Long customerId){

//        return orderMapper.
    }


}

