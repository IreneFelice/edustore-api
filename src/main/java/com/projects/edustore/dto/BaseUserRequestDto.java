package com.projects.edustore.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//TODO: add validations
public class BaseUserRequestDto {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username should be between 3 and 50 characters.")
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    @NotBlank(message = "Password is required")
    private String password;

//    Getters & Setters

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
