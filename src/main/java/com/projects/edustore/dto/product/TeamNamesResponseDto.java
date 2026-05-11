package com.projects.edustore.dto.product;

import java.util.List;

public class TeamNamesResponseDto {
    private List<String> teams;

    public TeamNamesResponseDto(List<String> teams) {
        this.teams = teams;
    }

    public List<String> getTeams() {
        return teams;
    }
}
