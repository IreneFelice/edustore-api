package com.projects.edustore.mapper;

import com.projects.edustore.dto.user.AuthenticatedResponseDto;
import com.projects.edustore.model.User;


public class AuthenticatedMapper {

    public static AuthenticatedResponseDto toResponse (User user) {
        AuthenticatedResponseDto response = new AuthenticatedResponseDto();
        response.setId(user.getId());
        response.setUserName(user.getUserName());
        response.setRole(user.getRole().name().replace("ROLE_", ""));
        return response;
    }

}
