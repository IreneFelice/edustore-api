package com.projects.edustore.mapper;

import com.projects.edustore.dto.UserRequestDto;
import com.projects.edustore.dto.UserResponseDto;
import com.projects.edustore.model.User;


public class UserMapper {

    public static User toEntity(UserRequestDto userRequestDto){
        User user = new User();
        user.setFirstName(userRequestDto.firstName);
        user.setLastName(userRequestDto.lastName);
        user.setEmail(userRequestDto.email);
        user.setPassword(userRequestDto.password);
        user.setRole(userRequestDto.role);
        user.setProfile(userRequestDto.profile);
        return user;
    }

    public static UserResponseDto toResponseDto(User user){
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.id = user.getId();
        userResponseDto.email = user.getEmail();
        userResponseDto.firstName = user.getFirstName();
        userResponseDto.lastName = user.getLastName();
        userResponseDto.profile = user.getProfile();
        return userResponseDto;
    }
}