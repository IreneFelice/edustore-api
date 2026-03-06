package com.projects.edustore.dto.profile;

import com.projects.edustore.dto.BaseUserRequestDto;
import jakarta.validation.constraints.Size;


public class StudentUserRequestDto extends BaseUserRequestDto {

    @Size(max = 50, message = "Name of team should be less than 50 characters.")
    private String team;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}

