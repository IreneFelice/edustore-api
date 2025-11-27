package com.projects.edustore.controller;

import com.projects.edustore.dto.BaseUserResponseDto;
import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.service.UserService;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("admin/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<BaseUserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // all students total or per schoolPeriod
    @GetMapping("/students")
    public ResponseEntity<List<StudentUserResponseDto>> getAllStudents(
            @RequestParam(required = false) List<String> schoolPeriod) {

        if (schoolPeriod == null || schoolPeriod.isEmpty()) {
            return ResponseEntity.ok(userService.getAllStudents());
        }

        return ResponseEntity.ok(userService.getStudentsByPeriods(schoolPeriod));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerUserResponseDto>> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseUserResponseDto> getUserById(
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<BaseUserResponseDto> getByEmail(
            @RequestParam @Email(message = "Email must be valid") String email) {
        return ResponseEntity.ok(userService.getByEmail(email));
    }

//    @PostMapping("/register") // newAdmin?
//    public ResponseEntity<BaseUserResponseDto> createUser(
//            @RequestBody AdminRequestDto adminRequestDto) {
//        return ResponseEntity.ok(userService.createUser(adminRequestDto));
//    }


    //update Admin
//    @PutMapping("/{id}")
//    public ResponseEntity<BaseUserResponseDto> updateUser(
//            @PathVariable Long id,
//            @RequestBody AdminRequestDto AdminRequestDto) {
//        return ResponseEntity.ok(userService.updateUser(id, AdminRequestDto));
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(
//            @PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
}





