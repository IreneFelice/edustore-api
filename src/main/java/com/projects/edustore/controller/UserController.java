package com.projects.edustore.controller;

import com.projects.edustore.dto.UserRequestDto;
import com.projects.edustore.dto.UserResponseDto;
import com.projects.edustore.mapper.UserMapper;
import com.projects.edustore.model.User;
import com.projects.edustore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

private final UserService userService;

public UserController(UserService userService){
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

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        User newUser = userService.createUser(userRequestDto);
        return ResponseEntity.ok(UserMapper.toResponseDto(newUser));
    }

}

