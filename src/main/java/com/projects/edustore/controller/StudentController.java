package com.projects.edustore.controller;

import com.projects.edustore.dto.profile.StudentUserRequestDto;
import com.projects.edustore.dto.profile.StudentUserResponseDto;
import com.projects.edustore.service.ProductService;
import com.projects.edustore.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService, ProductService productService) {
        this.studentService = studentService;
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
}
