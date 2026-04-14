package com.projects.edustore.mapper;

import com.projects.edustore.dto.BaseUserResponseDto;
import com.projects.edustore.model.User;


public class UserMapper {

    private static void fillBaseResponse (BaseUserResponseDto dto, User user) {
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setFirstName(user.getPerson().getFirstName());
        dto.setLastName(user.getPerson().getLastName());
        dto.setEmail(user.getPerson().getEmail());
        dto.setRole(user.getRole());
    }

    public static BaseUserResponseDto toBaseDto(User user) {
        BaseUserResponseDto dto = new BaseUserResponseDto();
        fillBaseResponse(dto, user);
        return dto;
    }

}