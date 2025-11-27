package com.projects.edustore.controller;

import com.projects.edustore.dto.productDto.ProductRequestDto;
import com.projects.edustore.dto.productDto.ProductCustomerResponseDto;
import com.projects.edustore.mapper.ProductMapper;
import com.projects.edustore.model.products.Product;
import com.projects.edustore.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shop")
public class ProductController {
    ProductRepository repos;

    public ProductController(ProductRepository repos) {
        this.repos = repos;
    }

    @GetMapping
    public ResponseEntity<List<ProductCustomerResponseDto>> getAllProducts() {
         List<Product> products = repos.findAll();

        List<ProductCustomerResponseDto> dtos = new ArrayList<>();
        for (Product product : products) {
            dtos.add(ProductMapper.toResponseDto(product));
        }
        return ResponseEntity.ok(dtos);
    }

    // Students only


    @PostMapping("/students")
    public ResponseEntity<ProductCustomerResponseDto> createProduct(@RequestBody ProductRequestDto dto) {
       Product newProduct = ProductMapper.toEntity(dto);
       repos.save(newProduct);
       return ResponseEntity.ok(ProductMapper.toResponseDto(newProduct));
    }
}

