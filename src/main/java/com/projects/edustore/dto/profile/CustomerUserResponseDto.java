package com.projects.edustore.dto.profile;


import com.projects.edustore.dto.user.BaseUserResponseDto;

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
