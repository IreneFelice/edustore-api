package com.projects.edustore.controller;

import com.projects.edustore.dto.product.ProductCustomerResponseDto;
import com.projects.edustore.model.product.Product;
import com.projects.edustore.repository.ProductRepository;
import com.projects.edustore.service.ProductService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {
    ProductRepository repos;
    ProductService productService;

    public ProductController(ProductRepository repos, ProductService productService) {
        this.repos = repos;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductCustomerResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProductsForCustomer());
    }

    @GetMapping("/team/{team}")
    public ResponseEntity<List<ProductCustomerResponseDto>> getProductsByTeam(@PathVariable String team) {
        return ResponseEntity.ok(productService.getProductsByTeam(team));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductCustomerResponseDto> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductForCustomer(productId));
    }

    ///////////////////////IMAGE//////////////////////////////////////////////////
    @GetMapping("/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
        Product product = productService.getProductWithImage(productId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(product.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + product.getOriginalFilename() + "\"")
                .body(product.getBytes());
    }


}

