package com.projects.edustore.mapper;

import com.projects.edustore.dto.UserRequestDto;
import com.projects.edustore.dto.UserResponseDto;
import com.projects.edustore.model.user.User;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;


public class UserMapper {

    public static void updateEntity(User existing, UserRequestDto dto) {
        existing.setUserName(dto.userName);
        existing.setFirstName(dto.firstName);
        existing.setLastName(dto.lastName);
        existing.setEmail(dto.email);
        existing.setPassword(dto.password);
        existing.setRole(dto.role);
        existing.setProfile(dto.profile);
    }

    public static User toEntity(UserRequestDto dto) {
        User user = new User();
        updateEntity(user, dto);
        return user;
    }

    public static UserResponseDto toResponseDto(User user){
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.id = user.getId();
        userResponseDto.email = user.getEmail();
        userResponseDto.userName = user.getUserName();
        userResponseDto.firstName = user.getFirstName();
        userResponseDto.lastName = user.getLastName();
        userResponseDto.role = user.getRole();
        userResponseDto.profile = user.getProfile();

        // add RESTful endpoints
        Map<String, String> links = new HashMap<>();
        String baseUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users")
                .toUriString();

        links.put("self, update, delete", baseUri + "/" + user.getId());
        links.put("allUsers", baseUri);
        userResponseDto.setLinks(links);

        return userResponseDto;
    }
}