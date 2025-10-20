package com.projects.edustore.dto.adminDto;

import com.projects.edustore.dto.BaseUserResponseDto;
import com.projects.edustore.model.Role;

public class AdminBaseResponseDto extends BaseUserResponseDto {
    private Role role;

//    Getters & Setters

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
