package com.projects.edustore.repository;

import com.projects.edustore.model.products.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    List<CartItem> findByCustomerId(Long id);
}
