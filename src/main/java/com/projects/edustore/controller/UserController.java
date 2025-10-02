package com.projects.edustore.controller;

import com.projects.edustore.dto.profileDto.CustomerUserRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;
import com.projects.edustore.dto.UserRequestDto;
import com.projects.edustore.dto.UserResponseDto;
import com.projects.edustore.dto.profileDto.StudentUserRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    //////////// Base User

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsersDto());
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponseDto> getByEmail(
            @RequestParam String email) {

        return ResponseEntity.ok(userService.getByEmail(email));
    }


    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody UserRequestDto userRequestDto) {

        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDto UserRequestDto) {

        return ResponseEntity.ok(userService.updateUser(id, UserRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    //    //  Customer ///////////////////////////////////////////////////////////
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerUserResponseDto>> getAllCustomers() {

        return ResponseEntity.ok(userService.getAllCustomers());
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerUserResponseDto> getCustomerById(
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.getCustomerById(id));
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerUserResponseDto> createCustomerUser(
            @RequestBody CustomerUserRequestDto customerUserRequestDto) {

        return ResponseEntity.ok(userService.createCustomerUser(customerUserRequestDto));
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerUserResponseDto> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerUserRequestDto dto) {

        return ResponseEntity.ok(userService.updateCustomer(id, dto));
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

////    Student endpoints //////////////////////////////////////////////////////////

    @GetMapping("/students")
    public ResponseEntity<List<StudentUserResponseDto>> getAllStudents() {

        return ResponseEntity.ok(userService.getAllStudents());
    }

    @GetMapping("/students/schoolperiod")
    public ResponseEntity<List<StudentUserResponseDto>> getStudentsBySchoolPeriod(@RequestParam String schoolPeriod) {

        return ResponseEntity.ok(userService.getStudentBySchoolPeriod(schoolPeriod));
    }


    @GetMapping("/students/{id}")
    public ResponseEntity getStudentById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.getStudentProfileById(id));
    }

    @PostMapping("/students")
    public ResponseEntity createStudentUser(@RequestBody StudentUserRequestDto studentUserRequestDto) {

        return ResponseEntity.ok(userService.createStudentUser(studentUserRequestDto));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentUserResponseDto> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentUserRequestDto studentUserRequestDto) {

        return ResponseEntity.ok(userService.updateStudentEntity(id, studentUserRequestDto));
    }

}





