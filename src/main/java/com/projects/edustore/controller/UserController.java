package com.projects.edustore.controller;

import com.projects.edustore.dto.BaseUserResponseDto;
import com.projects.edustore.dto.adminDto.AdminRequestDto;
import com.projects.edustore.dto.adminDto.AdminBaseResponseDto;
import com.projects.edustore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AdminBaseResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/students")
    public ResponseEntity<List<AdminBaseResponseDto>> getStudents(
            @RequestParam(required = false) List<String> schoolPeriod) {

        if (schoolPeriod == null || schoolPeriod.isEmpty()) {
            return ResponseEntity.ok(userService.getAllStudents());
        }

        return ResponseEntity.ok(userService.getStudentsByPeriods(schoolPeriod));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<AdminBaseResponseDto>> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllCustomers());
    }

    //    User without profile details
    @GetMapping("/{id}")
    public ResponseEntity<AdminBaseResponseDto> getUserById(
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //    including profile details
    @GetMapping("/{id}/profile")
    public ResponseEntity<AdminBaseResponseDto> getProfileDetailsById(
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfileDetailsById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<AdminBaseResponseDto> getByEmail(
            @RequestParam String email) {
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @PostMapping("/register")
    public ResponseEntity<BaseUserResponseDto> createUser(
            @RequestBody AdminRequestDto adminRequestDto) {
        return ResponseEntity.ok(userService.createUser(adminRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseUserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody AdminRequestDto AdminRequestDto) {
        return ResponseEntity.ok(userService.updateUser(id, AdminRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}





