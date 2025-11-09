package com.projects.edustore.service;

import com.projects.edustore.dto.BaseUserResponseDto;
import com.projects.edustore.dto.adminDto.AdminBaseResponseDto;
import com.projects.edustore.dto.adminDto.AdminRequestDto;

import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.CustomerMapper;
import com.projects.edustore.mapper.StudentMapper;
import com.projects.edustore.mapper.UserMapper;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.Person;
import com.projects.edustore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    private final UserRepository repos;
    private final PasswordEncoder passwordEncoder;
    private final StudentMapper studentMapper;
    private final CustomerMapper customerMapper;

    public UserService(UserRepository repos, StudentMapper studentMapper, PasswordEncoder passwordEncoder, CustomerMapper customerMapper) {
        this.repos = repos;
        this.passwordEncoder = passwordEncoder;
        this.studentMapper = studentMapper;
        this.customerMapper = customerMapper;
    }


    //////////// Security getUserDetails

    public User getUserByUsername(String username) {

        return repos.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
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

    public AdminBaseResponseDto getByEmail(String email) {
        User existing = repos.findByPerson_Email(email).orElseThrow(() -> new ResourceNotFoundException("User", email));
        return switch (existing.getRole()) {
            case ROLE_STUDENT -> UserMapper.toAdminStudentDto(existing);
            case ROLE_CUSTOMER -> UserMapper.toAdminCustomerDto(existing);
            default -> UserMapper.toAdminBaseDto(existing);
        };
    }

    @Transactional
    public BaseUserResponseDto updateUser(Long id, AdminRequestDto dto) {
        User existing = findUser(id);
        UserMapper.updateUserEntity(existing, dto);
        existing.setPassword(passwordEncoder.encode(dto.getPassword()));

        //attach profile based on role (helper)
        return attachProfileForRole(existing, dto);
    }

    @Transactional
    public BaseUserResponseDto createUser(AdminRequestDto dto) {
        User newUser = UserMapper.toUserEntity(dto);
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        //attach profile based on role (helper) + save user
        return attachProfileForRole(newUser, dto);

    }

    public void deleteUser(Long id) {
        User existing = repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
        repos.delete(existing);
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

    //   helpers
    private User findUser(Long id) {
        return repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }


    private BaseUserResponseDto attachProfileForRole(User user, AdminRequestDto dto) {
        Person person = user.getPerson();

        switch (user.getRole()) {
            case ROLE_CUSTOMER -> customerMapper.applyCustomerData(person, dto);
            case ROLE_STUDENT -> studentMapper.applyStudentData(person, dto);
            case ROLE_ADMIN -> {
            }
            default ->
                    throw new IllegalArgumentException("Unsupported role: " + user.getRole() + ". Role must be STUDENT, CUSTOMER or ADMIN");
        }


        repos.save(user);

        // return appropriate dto
        return switch (user.getRole()) {
            case ROLE_STUDENT -> UserMapper.toAdminStudentDto(user);
            case ROLE_CUSTOMER -> UserMapper.toAdminCustomerDto(user);
            default -> UserMapper.toAdminBaseDto(user);
        };
    }

}
