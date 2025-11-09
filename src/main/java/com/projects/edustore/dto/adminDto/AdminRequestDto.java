package com.projects.edustore.dto.adminDto;

import com.projects.edustore.dto.BaseUserRequestDto;
import com.projects.edustore.model.Role;
import jakarta.validation.constraints.Pattern;

public class AdminRequestDto extends BaseUserRequestDto {
    private Role role;

    @Pattern(
            regexp = "^[0-9]{7,15}$",
            message = "Phone number should contain only digits and be 7 to 15 characters long."
    )
    private String phoneNumber;

    private String schoolPeriod;

//    Getters & Setters

    public Role getRole() { return role; }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }
}