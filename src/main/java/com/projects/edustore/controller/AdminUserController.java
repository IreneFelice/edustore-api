package com.projects.edustore.controller;

import com.projects.edustore.service.AdminUserReadService;
import com.projects.edustore.dto.user.BaseUserResponseDto;
import com.projects.edustore.dto.profile.CustomerUserResponseDto;
import com.projects.edustore.dto.profile.StudentUserResponseDto;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableMethodSecurity
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("admin/users")
@Validated
public class AdminUserController {

    private final AdminUserReadService adminUserReadService;

    public AdminUserController(AdminUserReadService adminUserReadService) {
        this.adminUserReadService = adminUserReadService;
    }

    @GetMapping
    public ResponseEntity<List<BaseUserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminUserReadService.getAllUsers());
    }

    // get all students in total or in specific team(s)
    @GetMapping("/students")
    public ResponseEntity<List<StudentUserResponseDto>> getAllStudents(
            @RequestParam(required = false) List<String> teams) {
        return ResponseEntity.ok(adminUserReadService.getAllStudents(teams));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerUserResponseDto>> getAllCustomers() {
        return ResponseEntity.ok(adminUserReadService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseUserResponseDto> getUserById(
            @PathVariable Long id) {
        return ResponseEntity.ok(adminUserReadService.getUserById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<BaseUserResponseDto> geUserByEmail(
            @RequestParam @Email(message = "Email must be valid") String email) {
        return ResponseEntity.ok(adminUserReadService.getByEmail(email));
    }

}





