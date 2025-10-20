package com.projects.edustore.dto.adminDto;


public class AdminStudentResponseDto extends AdminBaseResponseDto {
    private String schoolPeriod;

    // getters & setters

    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }
}