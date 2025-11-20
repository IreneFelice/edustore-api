package com.projects.edustore.dto.profileDto;

import com.projects.edustore.dto.BaseUserRequestDto;
import jakarta.validation.constraints.Size;


public class StudentUserRequestDto extends BaseUserRequestDto {

    @Size(max = 50, message = "Name of SchoolPeriod should less than 50 characters.")
    private String schoolPeriod;

    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }
}

