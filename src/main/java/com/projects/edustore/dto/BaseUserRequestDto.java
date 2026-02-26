package com.projects.edustore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


public class BaseUserRequestDto {

    @Size(min = 3, max = 50, message = "Username should be between 3 and 50 characters.")
    private String userName;


    @Size(min = 2, message = "First name should be at least 2 characters long")
    @Size(max = 50, message = "First name should be less than 50 characters")
    private String firstName;

    @Size(min = 2, message = "First name should be at least 2 characters long")
    @Size(max = 50, message = "Last name should be less than 50 characters")
    private String lastName;

    @Size(min = 6, message = "Email should be valid")
    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 8, max = 100, message = "Password should be between 8 and 100 characters")
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
