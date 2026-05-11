package com.projects.edustore.dto.user;

public class AuthResponseDto {
    private String jwtToken;

    public AuthResponseDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}