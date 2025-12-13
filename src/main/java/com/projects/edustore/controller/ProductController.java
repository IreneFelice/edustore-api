package com.projects.edustore.controller;

import com.projects.edustore.dto.productDto.ProductRequestDto;
import com.projects.edustore.dto.productDto.ProductCustomerResponseDto;
import com.projects.edustore.dto.productDto.ProductStudentResponseDto;
import com.projects.edustore.repository.ProductRepository;
import com.projects.edustore.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ProductController {
    ProductRepository repos;
    ProductService productService;

    public ProductController(ProductRepository repos, ProductService productService) {
        this.repos = repos;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductCustomerResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/period/{schoolPeriod}")
    public ResponseEntity<List<ProductCustomerResponseDto>> getProductsBySchoolPeriod(@PathVariable String schoolPeriod) {
        return ResponseEntity.ok(productService.getProductsBySchoolPeriod(schoolPeriod));
    }


    // Students only

     @PostMapping("/students/{id}")
        public ResponseEntity<ProductStudentResponseDto> createProduct(
                @PathVariable Long id,
                @RequestBody ProductRequestDto dto) {
           return ResponseEntity.ok(productService.createNewProduct(dto, id));
        }

    @GetMapping("/students/{id}")
    public ResponseEntity<List<ProductStudentResponseDto>> getProductsByMaker(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductsByMaker(id));
    }


}

