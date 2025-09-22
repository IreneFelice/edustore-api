package com.projects.edustore.controller;

import com.projects.edustore.dto.UserRequestDto;
import com.projects.edustore.dto.UserResponseDto;
import com.projects.edustore.mapper.UserMapper;
import com.projects.edustore.model.user.User;
import com.projects.edustore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDto> response = new ArrayList<>();
        for (User user : users) {
            response.add(UserMapper.toResponseDto(user));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserMapper.toResponseDto(user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        User newUser = userService.createUser(userRequestDto);
        URI uri = URI.create("/users/" + newUser.getId());
        return ResponseEntity.created(uri).body(UserMapper.toResponseDto(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        User updated = userService.updateUser(id, userRequestDto);
        return ResponseEntity.ok(UserMapper.toResponseDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

