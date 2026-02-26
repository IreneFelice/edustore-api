package com.projects.edustore.controller;

import com.projects.edustore.dto.productDto.ProductRequestDto;
import com.projects.edustore.dto.productDto.ProductStudentResponseDto;
import com.projects.edustore.dto.profileDto.StudentUserRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.service.ProductService;
import com.projects.edustore.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final ProductService productService;

    public StudentController(StudentService studentService, ProductService productService) {
        this.studentService = studentService;
        this.productService = productService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentUserResponseDto> getStudentById(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    @GetMapping("/{studentId}/team")
    public ResponseEntity<List<StudentUserResponseDto>> getStudentsOwnTeam(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getByTeam(studentId));
    }

    @PostMapping("/register")
    public ResponseEntity<StudentUserResponseDto> createStudentUser(
            @Valid @RequestBody StudentUserRequestDto studentUserRequestDto) {
        return ResponseEntity.ok(studentService.createUser(studentUserRequestDto));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentUserResponseDto> updateStudent(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentUserRequestDto dto) {
        return ResponseEntity.ok(studentService.updateEntity(studentId, dto));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long studentId) {
        studentService.deleteUser(studentId);
        return ResponseEntity.noContent().build();
    }

    ///////////////////////PRODUCT///////////////////////////

    //1
    @GetMapping("/{studentId}/products")
    public ResponseEntity<List<ProductStudentResponseDto>> getProductsForMaker(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(productService.getAllProductsForMaker(studentId));
    }

    //2
    @GetMapping("/{studentId}/products/{productId}")
    public ResponseEntity<ProductStudentResponseDto> getProductForStudent(
            @PathVariable Long studentId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductForStudent(studentId, productId));
    }

    //3
    @PostMapping("/{studentId}/products")
    public ResponseEntity<ProductStudentResponseDto> createProduct(
            @PathVariable Long studentId,
            @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.createNewProduct(dto, studentId));
    }

    //4
    @PutMapping("/{studentId}/products/{productId}")
    public ResponseEntity<ProductStudentResponseDto> updateProduct(
            @PathVariable Long studentId,
            @PathVariable Long productId,
            @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.updateProductByMaker(studentId, productId, dto));
    }

    //5
    @DeleteMapping("/{studentId}/products/delete/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long studentId,
            @PathVariable Long productId) {
        productService.deleteProductByMaker(studentId, productId);
        return ResponseEntity.noContent().build();
    }

    //////// Product image //////////////


    @PutMapping("/{studentId}/products/{productId}/image")
    public ResponseEntity<String> uploadProductImage(
            @PathVariable Long studentId,
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) throws IOException {

        productService.uploadProductImageByMaker(studentId, productId, file);
        return ResponseEntity.ok("Image successfully uploaded.");
    }

    @DeleteMapping("/{studentId}/products/delete-product-image/{productId}")
    public ResponseEntity<String> deleteProductImage(
            @PathVariable Long studentId,
            @PathVariable Long productId) {
        productService.deleteProductImageByMaker(studentId, productId);
        return ResponseEntity.ok("Image deleted.");
    }

}
