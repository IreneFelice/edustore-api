package com.projects.edustore.admin;

import com.projects.edustore.dto.BaseUserResponseDto;

import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.CustomerMapper;
import com.projects.edustore.mapper.StudentMapper;
import com.projects.edustore.mapper.UserMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
class UserService {
    private final UserRepository repos;


    public UserService(UserRepository repos) {
        this.repos = repos;
    }

    //////////// Base User

    public List<BaseUserResponseDto> getAllUsers() {
        List<User> users = repos.findAll();
        List<BaseUserResponseDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(UserMapper.toBaseDto(user));
        }
        return dtos;
    }

    public BaseUserResponseDto getUserById(Long id) {
        User existing = findUser(id);
        return UserMapper.toBaseDto(existing);
    }

    public BaseUserResponseDto getByEmail(String email) {
        User existing = repos.findByPerson_Email(email.toLowerCase()).orElseThrow(() -> new ResourceNotFoundException("User", email));
        return switch (existing.getRole()) {
            case ROLE_STUDENT -> StudentMapper.toResponseDto(existing);
            case ROLE_CUSTOMER -> CustomerMapper.toResponseDto(existing);
            default -> UserMapper.toBaseDto(existing);
        };
    }

    //   helper
    private User findUser(Long id) {
        return repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

//    //////////// Students

    public List<StudentUserResponseDto> getAllStudents() {
        List<User> students = repos.findByRole(Role.ROLE_STUDENT);

        List<StudentUserResponseDto> dtos = new ArrayList<>();
        for (User user : students) {
            dtos.add(StudentMapper.toResponseDto(user));
        }
        return dtos;
    }

    public List<StudentUserResponseDto> getStudentsByTeams(List<String> teams) {
        List<String> lowerCaseTeams = new ArrayList<>();
        for (String team : teams) {
            lowerCaseTeams.add(team.toLowerCase());
        }
        List<User> students = repos.findByPerson_StudentProfile_TeamIn(lowerCaseTeams);
        List<StudentUserResponseDto> dtos = new ArrayList<>();

        for (User user : students) {
            dtos.add(StudentMapper.toResponseDto(user));
        }
        return dtos;
    }

    //    /////////// Customers

    public List<CustomerUserResponseDto> getAllCustomers() {
        List<User> customers = repos.findByRole(Role.ROLE_CUSTOMER);

        List<CustomerUserResponseDto> dtos = new ArrayList<>();
        for (User user : customers) {
            dtos.add(CustomerMapper.toResponseDto(user));
        }
        return dtos;
    }

}