package com.projects.edustore.dto.profileDto;

import com.projects.edustore.dto.UserResponseDto;

public class StudentUserResponseDto extends UserResponseDto {
    public String schoolPeriod;

    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }
}
