package com.projects.edustore.mapper;

import com.projects.edustore.dto.adminDto.AdminRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.person.Person;
import com.projects.edustore.model.person.StudentProfile;
import com.projects.edustore.model.User;

public class StudentMapper {

    public static User toEntity(StudentUserRequestDto dto) {
        User user = new User();
        user.setRole(Role.ROLE_STUDENT);
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());

        Person person = new Person();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setEmail(dto.getEmail());
        person.setProfileLabel("StudentProfile");

        user.setPerson(person);

        StudentProfile profile = new StudentProfile();
        profile.setSchoolPeriod(dto.getSchoolPeriod());

        person.setStudentProfile(profile);

        return user;
    }

    public static void updateEntity(User existing, StudentUserRequestDto dto) {
        Person person = existing.getPerson();
        //user
        if (dto.getUserName() != null) existing.setUserName(dto.getUserName());
        if (dto.getPassword() != null) existing.setPassword(dto.getPassword());
        //person
        if (dto.getFirstName() != null) person.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) person.setLastName(dto.getLastName());
        if (dto.getEmail() != null) person.setEmail(dto.getEmail());
        //profile
        if (dto.getSchoolPeriod() != null && person.getStudentProfile() != null) {
            person.getStudentProfile().setSchoolPeriod(dto.getSchoolPeriod());
        }
    }

    public static StudentUserResponseDto toResponseDto(User user) {
        StudentUserResponseDto dto = new StudentUserResponseDto();

        Person person = user.getPerson();

        dto.setId(user.getId());
        dto.setUserName(user.getUserName());

        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setEmail(person.getEmail());
        dto.setProfileLabel(person.getProfileLabel());

        if (person.getStudentProfile() != null) {
            dto.setSchoolPeriod(person.getStudentProfile().getSchoolPeriod());
        }
        return dto;
    }

    /////// used by Admin through UserService -> StudentService
    public static void applyStudentData(Person person, AdminRequestDto dto) {
        StudentProfile profile = person.getStudentProfile();
        if (profile == null) {
            profile = new StudentProfile();
            person.setStudentProfile(profile);
        }
        person.setProfileLabel("StudentProfile");
        if (dto.getSchoolPeriod() != null) {
            person.getStudentProfile().setSchoolPeriod(dto.getSchoolPeriod());
        }
    }
}
