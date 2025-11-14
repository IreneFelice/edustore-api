package com.projects.edustore.dto.profileDto;

import com.projects.edustore.dto.BaseUserRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public class StudentUserRequestDto extends BaseUserRequestDto {

    @NotBlank(message = "SchoolPeriod is required")
    @Size(min = 2, max = 50, message = "Name of SchoolPeriod should be between 2 and 50 characters.")
        private String schoolPeriod;

    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }
}

