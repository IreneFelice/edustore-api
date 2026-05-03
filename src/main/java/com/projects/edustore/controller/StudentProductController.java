package com.projects.edustore.controller;

import com.projects.edustore.dto.product.ProductRequestDto;
import com.projects.edustore.dto.product.ProductStudentResponseDto;
import com.projects.edustore.dto.product.ProductUpdateDto;
import com.projects.edustore.dto.product.UpdateProductMakerDto;
import com.projects.edustore.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/students/{studentId}/products")
public class StudentProductController {

        private final ProductService productService;

        public StudentProductController(ProductService productService) {
            this.productService = productService;
        }

        @GetMapping
        public ResponseEntity<List<ProductStudentResponseDto>> getProductsForMaker(
                @PathVariable Long studentId) {
            return ResponseEntity.ok(productService.getAllProductsForMaker(studentId));
        }

        @GetMapping("/{productId}")
        public ResponseEntity<ProductStudentResponseDto> getProductDetailsForTeam(
                @PathVariable Long studentId,
                @PathVariable Long productId) {
            return ResponseEntity.ok(productService.getProductDetailsForTeam(studentId, productId));
        }

        @PostMapping
        public ResponseEntity<ProductStudentResponseDto> createProduct(
                @PathVariable Long studentId,
                @Valid @RequestBody ProductRequestDto dto) {
            ProductStudentResponseDto response = productService.createNewProduct(dto, studentId);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/products/{id}")
                    .buildAndExpand(response.getId())
                    .toUri();
            return ResponseEntity.created(location).body(response);
        }

        @PatchMapping("/{productId}")
        public ResponseEntity<ProductStudentResponseDto> updateProduct(
                @PathVariable Long studentId,
                @PathVariable Long productId,
                @Valid @RequestBody ProductUpdateDto dto) {
            return ResponseEntity.ok(productService.updateProductByMaker(studentId, productId, dto));
        }

    @PatchMapping("/{productId}/maker")
    public ResponseEntity<String> updateProductMaker(
            @PathVariable Long studentId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductMakerDto dto) {

        productService.updateMaker(studentId, productId, dto);
        return ResponseEntity.ok("Product with id " + productId + ", now has maker with id " + dto.getNewMakerId());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long studentId,
            @PathVariable Long productId) {
        productService.deleteProductByMaker(studentId, productId);
        return ResponseEntity.noContent().build();
    }

        //////// Product image //////////////

        @PutMapping("/{productId}/image")
        public ResponseEntity<String> uploadProductImage(
                @PathVariable Long studentId,
                @PathVariable Long productId,
                @RequestParam("file") MultipartFile file) throws IOException {

            productService.uploadProductImageByMaker(studentId, productId, file);
            return ResponseEntity.ok("Image successfully uploaded.");
        }

        @DeleteMapping("/{productId}/image")
        public ResponseEntity<Void> deleteProductImage(
                @PathVariable Long studentId,
                @PathVariable Long productId) {
            productService.deleteProductImageByMaker(studentId, productId);
            return ResponseEntity.noContent().build();
        }
    }