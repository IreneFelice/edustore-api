package com.projects.edustore.service;

import com.projects.edustore.dto.adminDto.AdminBaseResponseDto;
import com.projects.edustore.dto.adminDto.AdminRequestDto;

import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.UserMapper;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    private final UserRepository repos;
    private final StudentService studentService;
    private final CustomerService customerService;

    public UserService(UserRepository repos, StudentService studentService, CustomerService customerService) {
        this.repos = repos;
        this.studentService = studentService;
        this.customerService = customerService;
    }

    //////////// Base User

    public List<AdminBaseResponseDto> getAllUsers() {
        List<User> users = repos.findAll();
        List<AdminBaseResponseDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(UserMapper.toAdminBaseDto(user));
        }
        return dtos;
    }

    public AdminBaseResponseDto getUserById(Long id) {
        User existing = findUser(id);
        return UserMapper.toAdminBaseDto(existing);
    }

    public AdminBaseResponseDto getProfileDetailsById(Long id) {
        User existing = findUser(id);
        return switch (existing.getRole()) {
            case ROLE_STUDENT -> UserMapper.toAdminStudentDto(existing);
            case ROLE_CUSTOMER -> UserMapper.toAdminCustomerDto(existing);
            default -> UserMapper.toAdminBaseDto(existing);
        };
    }

    public AdminBaseResponseDto updateUser(Long id, AdminRequestDto dto) {
        User existing = findUser(id);
        UserMapper.updateUserEntity(existing, dto);
        repos.save(existing);

        switch (existing.getRole()) {
            case ROLE_CUSTOMER -> customerService.attachProfile(existing, dto);
            case ROLE_STUDENT -> studentService.attachProfile(existing, dto);
            case ROLE_ADMIN -> {
            }
            default -> throw new IllegalArgumentException("Unsupported role: " + existing.getRole() + ". Role must be STUDENT, CUSTOMER or ADMIN");
        }

        return switch (existing.getRole()) {
            case ROLE_STUDENT -> UserMapper.toAdminStudentDto(existing);
            case ROLE_CUSTOMER -> UserMapper.toAdminCustomerDto(existing);
            default -> UserMapper.toAdminBaseDto(existing);
        };
    }

    public AdminBaseResponseDto getByEmail(String email) {
        User existing = repos.findByPerson_Email(email).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return switch (existing.getRole()) {
            case ROLE_STUDENT -> UserMapper.toAdminStudentDto(existing);
            case ROLE_CUSTOMER -> UserMapper.toAdminCustomerDto(existing);
            default -> UserMapper.toAdminBaseDto(existing);
        };
    }

    public void deleteUser(Long id) {
        User existing = repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("User" + id + "not found."));
        repos.delete(existing);
    }

    @Transactional
    public AdminBaseResponseDto createUser(AdminRequestDto dto) {
        User NewUser = createUserEntity(dto);

        //Attach profile based on Role
        switch (NewUser.getRole()) {
            case ROLE_CUSTOMER -> customerService.attachProfile(NewUser, dto);
            case ROLE_STUDENT -> studentService.attachProfile(NewUser, dto);
            case ROLE_ADMIN -> {
            }
            default ->
                    throw new IllegalArgumentException("Unsupported role: " + NewUser.getRole() + ". Role must be STUDENT, CUSTOMER or ADMIN");
        }

        repos.save(NewUser);

        return switch (NewUser.getRole()) {
            case ROLE_STUDENT -> UserMapper.toAdminStudentDto(NewUser);
            case ROLE_CUSTOMER -> UserMapper.toAdminCustomerDto(NewUser);
            default -> UserMapper.toAdminBaseDto(NewUser);
        };
    }


//    //////////// Students

    public List<AdminBaseResponseDto> getAllStudents() {
        List<User> students = repos.findByPerson_ProfileLabel("StudentProfile");
        List<AdminBaseResponseDto> dtos = new ArrayList<>();
        for (User user : students) {
            dtos.add(UserMapper.toAdminStudentDto(user));
        }
        return dtos;
    }

    public List<AdminBaseResponseDto> getStudentsByPeriods(List<String> periods) {
        List<User> students = repos.findByPerson_StudentProfile_SchoolPeriodIn(periods);
        List<AdminBaseResponseDto> dtos = new ArrayList<>();

        for (User user : students) {
            dtos.add(UserMapper.toAdminStudentDto(user));
        }

        return dtos;
    }

    //    /////////// Customers
    public List<AdminBaseResponseDto> getAllCustomers() {
        List<User> customers = repos.findByPerson_ProfileLabel("CustomerProfile");
        List<AdminBaseResponseDto> dtos = new ArrayList<>();
        for (User user : customers) {
            dtos.add(UserMapper.toAdminCustomerDto(user));
        }
        return dtos;
    }


    //   utils
    public User findUser(Long id) {
        return repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("User" + id + "not found."));
    }

    public User createUserEntity(AdminRequestDto adminRequestDto) {
        User newUser = UserMapper.toUserEntity(adminRequestDto);
        repos.save(newUser);
        return newUser;
    }

}
