package com.projects.edustore.dto.profileDto;

import com.projects.edustore.dto.BaseUserRequestDto;

public class CustomerUserRequestDto extends BaseUserRequestDto {
    private String phoneNumber;

    //    Getters & Setters

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}