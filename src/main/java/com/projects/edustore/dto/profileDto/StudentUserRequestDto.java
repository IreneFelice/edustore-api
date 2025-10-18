package com.projects.edustore.dto.profileDto;

import com.projects.edustore.dto.BaseUserRequestDto;

public class StudentUserRequestDto extends BaseUserRequestDto {
        private String schoolPeriod;

    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }
}

