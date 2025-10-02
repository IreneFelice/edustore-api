package com.projects.edustore.dto.profileDto;

import com.projects.edustore.dto.UserRequestDto;

public class CustomerUserRequestDto extends UserRequestDto {
    public String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}