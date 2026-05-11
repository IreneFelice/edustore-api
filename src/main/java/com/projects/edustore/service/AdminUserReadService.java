package com.projects.edustore.service;

import com.projects.edustore.dto.user.BaseUserResponseDto;

import com.projects.edustore.dto.profile.CustomerUserResponseDto;
import com.projects.edustore.dto.profile.StudentUserResponseDto;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.person.CustomerMapper;
import com.projects.edustore.mapper.person.StudentMapper;
import com.projects.edustore.mapper.UserMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AdminUserReadService {
    private final UserRepository repos;

    public AdminUserReadService(UserRepository repos) {
        this.repos = repos;
    }

    public List<BaseUserResponseDto> getAllUsers() {
        List<User> users = repos.findAll();
        List<BaseUserResponseDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(UserMapper.toBaseDto(user));
        }
        return dtos;
    }

    public BaseUserResponseDto getUserById(Long id) {
        User existing = repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
        return switch (existing.getRole()) {
            case ROLE_STUDENT -> StudentMapper.toResponseDto(existing);
            case ROLE_CUSTOMER -> CustomerMapper.toResponseDto(existing);
            default -> UserMapper.toBaseDto(existing);
        };
    }

    public BaseUserResponseDto getByEmail(String email) {
        User existing = repos.findByPerson_Email(email.toLowerCase()).orElseThrow(() -> new ResourceNotFoundException("User", email));
        return switch (existing.getRole()) {
            case ROLE_STUDENT -> StudentMapper.toResponseDto(existing);
            case ROLE_CUSTOMER -> CustomerMapper.toResponseDto(existing);
            default -> UserMapper.toBaseDto(existing);
        };
    }

    public List<StudentUserResponseDto> getAllStudents(List<String> teams) {
        List<User> students;

        if (teams == null || teams.isEmpty()) {
            students = repos.findByRole(Role.ROLE_STUDENT);
        } else {
            List<String> lowerCaseTeams = new ArrayList<>();
            for (String team : teams) {
                lowerCaseTeams.add(team.toLowerCase());
            }
            students = repos.findByPerson_StudentProfile_TeamIn(lowerCaseTeams);
        }
        List<StudentUserResponseDto> dtos = new ArrayList<>();
        for (User user : students) {
            dtos.add(StudentMapper.toResponseDto(user));
        }
        return dtos;
    }

    public List<CustomerUserResponseDto> getAllCustomers() {
        List<User> customers = repos.findByRole(Role.ROLE_CUSTOMER);

        List<CustomerUserResponseDto> dtos = new ArrayList<>();
        for (User user : customers) {
            dtos.add(CustomerMapper.toResponseDto(user));
        }
        return dtos;
    }
}