package com.projects.edustore.mapper;

import com.projects.edustore.dto.adminDto.AdminCustomerResponseDto;
import com.projects.edustore.dto.adminDto.AdminRequestDto;
import com.projects.edustore.dto.adminDto.AdminBaseResponseDto;
import com.projects.edustore.dto.adminDto.AdminStudentResponseDto;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.Person;


//    TODO make link builder utility

public class UserMapper {


    public static User toUserEntity(AdminRequestDto dto) {
        User user = new User();
        Person person = new Person();

        user.setUserName(dto.getUserName());
//        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole() != null ? dto.getRole() : Role.ROLE_CUSTOMER); // default is customer
        user.setPerson(person);

        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setEmail(dto.getEmail());

        return user;
    }

    public static void updateUserEntity(User existing, AdminRequestDto dto) {
        Person person = existing.getPerson();

        if (dto.getRole() != null) existing.setRole(dto.getRole());
        if (dto.getUserName() != null) existing.setUserName(dto.getUserName());
        if (dto.getFirstName() != null) person.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) person.setLastName(dto.getLastName());
        if (dto.getEmail() != null) person.setEmail(dto.getEmail());
        if (dto.getSchoolPeriod() != null) person.getStudentProfile().setSchoolPeriod(dto.getSchoolPeriod());
        if (dto.getPhoneNumber() != null) person.getCustomerProfile().setPhoneNumber(dto.getPhoneNumber());
    }

    private static void fillBaseResponse (AdminBaseResponseDto dto, User user) {
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setFirstName(user.getPerson().getFirstName());
        dto.setLastName(user.getPerson().getLastName());
        dto.setEmail(user.getPerson().getEmail());
        dto.setProfileLabel(user.getPerson().getProfileLabel());
        dto.setRole(user.getRole());
    }

    public static AdminBaseResponseDto toAdminBaseDto(User user) {
        AdminBaseResponseDto dto = new AdminBaseResponseDto();
        fillBaseResponse(dto, user);
        return dto;
    }

    public static AdminStudentResponseDto toAdminStudentDto(User user) {
        AdminStudentResponseDto dto = new AdminStudentResponseDto();
        fillBaseResponse(dto, user);
        if (user.getPerson().getStudentProfile() != null) {
            dto.setSchoolPeriod(user.getPerson().getStudentProfile().getSchoolPeriod());
        }
        return dto;
    }

    public static AdminCustomerResponseDto toAdminCustomerDto(User user) {
        AdminCustomerResponseDto dto = new AdminCustomerResponseDto();
        fillBaseResponse(dto, user);
        if (user.getPerson().getCustomerProfile() != null) {
            dto.setPhoneNumber(user.getPerson().getCustomerProfile().getPhoneNumber());
        }
        return dto;
    }

}