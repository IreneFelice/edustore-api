package com.projects.edustore.dto.adminDto;

import com.projects.edustore.dto.BaseUserRequestDto;
import com.projects.edustore.model.Role;
//TODO: add validations
public class AdminRequestDto extends BaseUserRequestDto {
    private Role role;

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