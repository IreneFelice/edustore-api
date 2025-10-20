package com.projects.edustore.dto.profileDto;

import com.projects.edustore.dto.BaseUserResponseDto;

public class StudentUserResponseDto extends BaseUserResponseDto {
    private String schoolPeriod;

    //    Getters & Setters
    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }

}
