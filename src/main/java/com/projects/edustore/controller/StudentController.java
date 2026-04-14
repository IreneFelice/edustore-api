package com.projects.edustore.controller;

import com.projects.edustore.dto.profile.StudentUserRequestDto;
import com.projects.edustore.dto.profile.StudentUserResponseDto;
import com.projects.edustore.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentUserResponseDto> getStudentById(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    // get all existing team names through ProductController - getAllUniqueTeamNames()
    @GetMapping("/{studentId}/team")
    public ResponseEntity<List<StudentUserResponseDto>> getStudentsInOwnTeam(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getByTeam(studentId));
    }

    @PostMapping("/register")
    public ResponseEntity<StudentUserResponseDto> createStudentUser(
            @Valid @RequestBody StudentUserRequestDto studentUserRequestDto) {
        StudentUserResponseDto response = studentService.createStudentUser(studentUserRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/students/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
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
