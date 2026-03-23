package com.projects.edustore.repository;

import com.projects.edustore.model.product.Order;
import com.projects.edustore.model.product.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);

    List<Order> findAllByStatus(OrderStatus status);
}
