package com.projects.edustore.dto.profile;

import com.projects.edustore.dto.BaseUserRequestDto;
import jakarta.validation.constraints.Size;

public class StudentUserRequestDto extends BaseUserRequestDto {
    @Size(min = 2, max = 30, message = "Team name should be between 2 and 30 characters")
    private String team;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}

