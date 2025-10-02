package com.projects.edustore.mapper;

import com.projects.edustore.dto.profileDto.CustomerUserRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;
import com.projects.edustore.dto.UserRequestDto;
import com.projects.edustore.dto.UserResponseDto;
import com.projects.edustore.dto.profileDto.StudentUserRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.user.CustomerProfile;
import com.projects.edustore.model.user.StudentProfile;
import com.projects.edustore.model.user.User;



//    TODO make link builder utility

public class UserMapper {

    //  Base User
    public static User toUserEntity(UserRequestDto dto) {
        User user = new User();
        updateUserEntity(user, dto);
        return user;
    }

    public static void updateUserEntity(User existing, UserRequestDto dto) {
        if (dto.userName != null) existing.setUserName(dto.userName);
        if (dto.firstName != null) existing.setFirstName(dto.firstName);
        if (dto.lastName != null) existing.setLastName(dto.lastName);
        if (dto.email != null) existing.setEmail(dto.email);
//        if (dto.password != null) existing.setPassword(dto.password);
        if (dto.role != null) existing.setRole(dto.role);
        if (dto.profile != null) existing.setProfileLabel(dto.profile);
    }

    public static UserResponseDto toUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        fillBaseResponse(user, dto);
        return dto;
    }

    //   Helper Base User
    private static void fillBaseResponse(User user, UserResponseDto dto) {
        dto.id = user.getId();
        dto.email = user.getEmail();
        dto.userName = user.getUserName();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.role = user.getRole();
        dto.profile = user.getProfileLabel();
    }


    //////////////////////////  Customer mapping
    public static User toCustomerEntity(CustomerUserRequestDto dto) {
        User user = toUserEntity(dto); //new base user

        user.setProfileLabel("CustomerProfile");
        user.setRole(Role.ROLE_CUSTOMER);

        CustomerProfile profile = new CustomerProfile();
        profile.setPhoneNumber(dto.phoneNumber);

        profile.setUser(user);
        user.setCustomerProfile(profile);

        return user;
    }

    public static User updateCustomerEntity(User existingCustomer, CustomerUserRequestDto dto) {

        existingCustomer.setUserName(dto.userName);
        existingCustomer.setFirstName(dto.firstName);
        existingCustomer.setLastName(dto.lastName);
        existingCustomer.setEmail(dto.email);
        existingCustomer.setPassword(dto.password);
        existingCustomer.getCustomerProfile().setPhoneNumber(dto.phoneNumber);

        return existingCustomer;
    }


    public static CustomerUserResponseDto toCustomerResponseDto(User user) {
        CustomerUserResponseDto dto = new CustomerUserResponseDto();
        fillBaseResponse(user, dto);
        dto.phoneNumber = user.getCustomerProfile().getPhoneNumber();

        return dto;
    }

//////////////////////    Student mapping

    public static User toStudentEntity(StudentUserRequestDto dto) {
        User user = toUserEntity(dto);
        user.setProfileLabel("StudentProfile");
        user.setRole(Role.ROLE_STUDENT);

        StudentProfile profile = new StudentProfile();
        profile.setSchoolPeriod(dto.schoolPeriod);

        profile.setUser(user);
        user.setStudentProfile(profile);

        return user;
    }

    public static User updateStudentEntity(User existingStudent, StudentUserRequestDto dto){
        existingStudent.setUserName(dto.userName);
        existingStudent.setFirstName(dto.firstName);
        existingStudent.setLastName(dto.lastName);
        existingStudent.setEmail(dto.email);
        existingStudent.setPassword(dto.password);
        existingStudent.getStudentProfile().setSchoolPeriod(dto.schoolPeriod);

        return existingStudent;
    }


    public static StudentUserResponseDto toStudentResponseDto(User user) {
        StudentUserResponseDto dto = new StudentUserResponseDto();
        fillBaseResponse(user, dto);

            dto.schoolPeriod = user.getStudentProfile().getSchoolPeriod();

        return dto;
    }


}