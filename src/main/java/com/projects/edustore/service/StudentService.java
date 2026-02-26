package com.projects.edustore.service;

import com.projects.edustore.dto.profileDto.StudentUserRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.mapper.StudentMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class StudentService {

    private final StudentRepository repos;
    private final PasswordEncoder passwordEncoder;
    private final WhoCanSeeWhoService whoCanSee;

    public StudentService(StudentRepository repos, PasswordEncoder passwordEncoder, WhoCanSeeWhoService whoCanSee) {
        this.repos = repos;
        this.passwordEncoder = passwordEncoder;
        this.whoCanSee = whoCanSee;
    }

    public User findAndAuthorizeStudent(Long id) {
        return whoCanSee.findUserAndCheckPermission(id, Role.ROLE_STUDENT, "Student");
    }

    public StudentUserResponseDto getStudentById(Long id) {
        User user = findAndAuthorizeStudent(id);
        return StudentMapper.toResponseDto(user);
    }

    public List<StudentUserResponseDto> getByTeam(Long id) {
        User user = findAndAuthorizeStudent(id);
        String team = user.getPerson().getStudentProfile().getTeam();

        List<User> students = repos.findByPerson_StudentProfile_Team(team);
        List<StudentUserResponseDto> dtos = new ArrayList<>();
        for (User student : students) {
            dtos.add(StudentMapper.toResponseDto(student));
        }
        return dtos;
    }

    @Transactional
    public StudentUserResponseDto createUser(StudentUserRequestDto dto) {
        String hashed = passwordEncoder.encode(dto.getPassword());

        User newStudent = StudentMapper.toEntity(dto);
        newStudent.setPassword(hashed);

        User savedUser = repos.save(newStudent);

        return StudentMapper.toResponseDto(savedUser);
    }

    @Transactional
    public StudentUserResponseDto updateEntity(Long id, StudentUserRequestDto dto) {
        User existingStudent = findAndAuthorizeStudent(id);

        StudentMapper.updateEntity(existingStudent, dto);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingStudent.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        repos.save(existingStudent);
        return StudentMapper.toResponseDto(existingStudent);
    }

    public void deleteUser(Long id) {
        User existingStudent = findAndAuthorizeStudent(id);
        repos.delete(existingStudent);
    }

}


