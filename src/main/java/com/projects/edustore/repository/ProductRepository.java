package com.projects.edustore.repository;

import com.projects.edustore.model.product.journey.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByMaker_Id(Long id);
    List<Product> findByMaker_TeamIgnoreCase(String team);
}
