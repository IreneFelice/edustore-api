package com.projects.edustore.controller;

import com.projects.edustore.dto.profileDto.StudentUserRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentUserResponseDto> getStudentById(
            @PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/{id}/schoolperiod")
    public ResponseEntity<List<StudentUserResponseDto>> getStudentsBySharedSchoolPeriod(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getBySchoolPeriod(id));
    }

    @PostMapping("/register")
    public ResponseEntity<StudentUserResponseDto> createStudentUser(
            @RequestBody StudentUserRequestDto studentUserRequestDto) {
        return ResponseEntity.ok(studentService.createUser(studentUserRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentUserResponseDto> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentUserRequestDto dto) {
        return ResponseEntity.ok(studentService.updateEntity(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id) {
        studentService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
