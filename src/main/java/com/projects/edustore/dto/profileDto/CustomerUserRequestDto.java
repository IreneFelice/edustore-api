package com.projects.edustore.dto.profileDto;

import com.projects.edustore.dto.BaseUserRequestDto;
import jakarta.validation.constraints.Pattern;

public class CustomerUserRequestDto extends BaseUserRequestDto {

    @Pattern(
            regexp = "^[0-9]{7,15}$",
            message = "Phone number should contain only digits and be 7 to 15 characters long."
    )
    private String phoneNumber;

    //    Getters & Setters

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}