package com.projects.edustore.dto.profileDto;


import com.projects.edustore.dto.BaseUserResponseDto;

public class CustomerUserResponseDto extends BaseUserResponseDto {
    private String phoneNumber;

    //    Getters & Setters

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
