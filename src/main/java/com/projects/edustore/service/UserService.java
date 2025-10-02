package com.projects.edustore.service;

import com.projects.edustore.dto.UserResponseDto;
import com.projects.edustore.dto.UserRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;
import com.projects.edustore.dto.profileDto.StudentUserRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.UserMapper;
import com.projects.edustore.model.user.User;
import com.projects.edustore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    private final UserRepository repos;

    public UserService(UserRepository repos) {
        this.repos = repos;
    }

    //////////// Base User

    public List<UserResponseDto> getAllUsersDto() {
        List<User> users = repos.findAll();
        List<UserResponseDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(UserMapper.toUserResponseDto(user));
        }
        return dtos;
    }

    //   util
    public User findUser(Long id) {
        return repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("User" + id + "not found."));
    }

    public UserResponseDto getUserById(Long id) {
        User existing = findUser(id);
        return UserMapper.toUserResponseDto(existing);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User existing = findUser(id);
        UserMapper.updateUserEntity(existing, dto);
        repos.save(existing);
        return UserMapper.toUserResponseDto(existing);
    }

    public UserResponseDto getByEmail(String email) {
        User user = repos.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return UserMapper.toUserResponseDto(user);
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User newUser = UserMapper.toUserEntity(userRequestDto);
        repos.save(newUser);
        return UserMapper.toUserResponseDto(newUser);
    }

     public void deleteUser(Long id) {
        User existing = repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("User" + id + "not found."));
        repos.delete(existing);
    }

//    /////////// Customers

    public List<CustomerUserResponseDto> getAllCustomers() {
        List<User> customers = repos.findByProfileLabel("CustomerProfile");
        List<CustomerUserResponseDto> dtos = new ArrayList<>();
        for (User user : customers) {
            dtos.add(UserMapper.toCustomerResponseDto(user));
        }
         return dtos;
    }

    public CustomerUserResponseDto getCustomerById(Long id) {
       User user = findUser(id);
       return  UserMapper.toCustomerResponseDto(user);
    }

    public CustomerUserResponseDto createCustomerUser(CustomerUserRequestDto customerUserRequestDto) {
        User newCustomer = UserMapper.toCustomerEntity(customerUserRequestDto);
        repos.save(newCustomer);
        return UserMapper.toCustomerResponseDto(newCustomer);
    }

    public CustomerUserResponseDto updateCustomer(Long id, CustomerUserRequestDto dto) {
        User existingCustomer = findUser(id);
        if (existingCustomer.getCustomerProfile() != null) {
            UserMapper.updateCustomerEntity(existingCustomer, dto);
        }
        repos.save(existingCustomer);
        return UserMapper.toCustomerResponseDto(existingCustomer);
    }

//    //////////// Students

    public List<StudentUserResponseDto> getAllStudents() {
        List<User> students = repos.findByProfileLabel("StudentProfile");
        List<StudentUserResponseDto> dtos = new ArrayList<>();
        for(User user : students) {
            dtos.add(UserMapper.toStudentResponseDto(user));
        }
        return dtos;
    }

    public StudentUserResponseDto getStudentProfileById(Long id) {
        User user = findUser(id);
        return  UserMapper.toStudentResponseDto(user);
    }

    public List<StudentUserResponseDto> getStudentBySchoolPeriod(String schoolPeriod) {
        List<User> students = repos.findByStudentProfile_schoolPeriodIgnoreCase(schoolPeriod);
        List<StudentUserResponseDto> dtos = new ArrayList<>();
        for(User user : students){
            dtos.add(UserMapper.toStudentResponseDto(user));
        }
        return dtos;
    }

    public StudentUserResponseDto createStudentUser(StudentUserRequestDto dto) {
        User newStudent = UserMapper.toStudentEntity(dto);
        repos.save(newStudent);
        return UserMapper.toStudentResponseDto(newStudent);
    }

    public StudentUserResponseDto updateStudentEntity(Long id, StudentUserRequestDto dto) {
        User existingStudent = findUser(id);
        UserMapper.updateStudentEntity(existingStudent, dto);
        repos.save(existingStudent);

        return UserMapper.toStudentResponseDto(existingStudent);
    }

}
