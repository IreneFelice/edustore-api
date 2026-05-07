package com.projects.edustore.dto.profile;

import com.projects.edustore.dto.user.BaseUserRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CustomerUserRequestDto extends BaseUserRequestDto {

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{7,15}$",
            message = "Phone number should contain only digits and should be between 7 to 15 characters"
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