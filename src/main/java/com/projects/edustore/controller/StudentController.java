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

    @GetMapping("/{studentId}/schoolperiod")
    public ResponseEntity<List<StudentUserResponseDto>> getStudentsBySharedSchoolPeriod(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getBySchoolPeriod(studentId));
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

//    @GetMapping("/{studentId}/products")
//    public ResponseEntity<List<ProductStudentResponseDto>> getProductsByStudentId(@PathVariable Long studentId) {
//        return ResponseEntity.ok(studentService.getProducts(studentId));
//    }

    @GetMapping("/{studentId}/products")
    public ResponseEntity<List<ProductStudentResponseDto>> getProductsByMaker(@PathVariable Long studentId) {
        return ResponseEntity.ok(productService.getAllProductsByMaker(studentId));
    }
    @PostMapping("/{studentId}/products")
    public ResponseEntity<ProductStudentResponseDto> createProduct(
            @PathVariable Long studentId,
            @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.createNewProduct(dto, studentId));
    }

    @PutMapping("/{studentId}/products/{productId}/image")
    public ResponseEntity<Void> uploadProductImage(
            @PathVariable Long studentId,
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) throws IOException {

        productService.uploadProductImageByMaker(studentId, productId, file);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/{studentId}/products/{productId}/image")
//    public ResponseEntity<byte[]> getProductImage(
//            @PathVariable Long studentId,
//            @PathVariable Long productId){
//
//
//    }



}
