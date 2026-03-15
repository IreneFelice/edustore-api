package com.projects.edustore.repository;

import com.projects.edustore.model.product.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
