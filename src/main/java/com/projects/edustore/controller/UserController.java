package com.projects.edustore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/users")
    public String testEndpoint(@RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) {
            return "your name is unknown";
        } else {
            return "your name is " + name;
        }
    }

}
