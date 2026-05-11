package com.projects.edustore.mapper.person;

import com.projects.edustore.dto.profile.StudentUserRequestDto;
import com.projects.edustore.dto.profile.StudentUserResponseDto;
import com.projects.edustore.dto.profile.StudentUserUpdateDto;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.person.Person;
import com.projects.edustore.model.person.StudentProfile;
import com.projects.edustore.model.User;


public class StudentMapper {

    public static User toEntity(StudentUserRequestDto dto) {
        User user = new User();
        user.setRole(Role.ROLE_STUDENT);
        user.setUserName(dto.getUserName());

        Person person = Person.create(user, dto.getFirstName(), dto.getLastName(), dto.getEmail());
        String team = dto.getTeam().trim().toLowerCase();
        StudentProfile.create(person, team);

        return user;
    }

    public static void updateEntity(User existing, StudentUserUpdateDto dto) {
        Person person = existing.getPerson();

        //user
        if (dto.getUserName() != null) existing.setUserName(dto.getUserName());
        //person
        if (dto.getFirstName() != null) person.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) person.setLastName(dto.getLastName());
        if (dto.getEmail() != null) person.setEmail(dto.getEmail());
        //profile
        if (dto.getTeam() != null && person.getStudentProfile() != null) {
            String team = dto.getTeam().trim().toLowerCase();
            person.getStudentProfile().setTeam(team);
        }
    }

    public static StudentUserResponseDto toResponseDto(User user) {
        StudentUserResponseDto dto = new StudentUserResponseDto();

        Person person = user.getPerson();
        dto.setRole(person.getUser().getRole());
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());

        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setEmail(person.getEmail());

        if (person.getStudentProfile() != null) {
            dto.setTeam(person.getStudentProfile().getTeam());
        }
        return dto;
    }
}
