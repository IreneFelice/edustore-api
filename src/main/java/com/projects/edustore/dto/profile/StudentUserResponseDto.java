package com.projects.edustore.dto.profile;

import com.projects.edustore.dto.BaseUserResponseDto;


public class StudentUserResponseDto extends BaseUserResponseDto {
    private String team;

    //    Getters & Setters
    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

}
