package com.projects.edustore.controller;

import com.projects.edustore.dto.productDto.ProductCustomerResponseDto;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.model.products.Product;
import com.projects.edustore.repository.ProductRepository;
import com.projects.edustore.service.ProductService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{schoolPeriod}")
    public ResponseEntity<List<ProductCustomerResponseDto>> getProductsBySchoolPeriod(@PathVariable String schoolPeriod) {
        return ResponseEntity.ok(productService.getProductsBySchoolPeriod(schoolPeriod));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}/image")
    public ResponseEntity<Void> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) throws IOException {

        productService.uploadProductImage(productId, file);
        return ResponseEntity.ok().build();
    }

    /////////////////////////////////////////////////////////////////////////


    @GetMapping("/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
        Product product = repos.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        if (product.getBytes() == null) {
            throw new ResourceNotFoundException("Image for product", productId);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(product.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + product.getOriginalFilename() + "\"")
                .body(product.getBytes());
    }


}

