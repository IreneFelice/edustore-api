package com.projects.edustore.service;

import com.projects.edustore.dto.profileDto.StudentUserRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.StudentMapper;
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

    public StudentService(StudentRepository repos, PasswordEncoder passwordEncoder) {
        this.repos = repos;
        this.passwordEncoder = passwordEncoder;
    }

    public User findStudent(Long id) {
        return repos.findByIdAndPerson_ProfileLabel(id, "StudentProfile")
                .orElseThrow(() -> new ResourceNotFoundException("Student " + id + " not found."));
    }
    public StudentUserResponseDto getStudentById(Long id) {
        User user = findStudent(id);

        return StudentMapper.toResponseDto(user);
    }

//    public List<StudentUserResponseDto> getAllStudents() {
//        List<User> students = repos.findByPerson_ProfileLabel("StudentProfile");
//        List<StudentUserResponseDto> dtos = new ArrayList<>();
//        for (User user : students) {
//            dtos.add(StudentMapper.toResponseDto(user));
//        }
//        return dtos;
//    }


    public List<StudentUserResponseDto> getBySchoolPeriod(Long id) {
        User user = findStudent(id);
        String schoolPeriod = user.getPerson().getStudentProfile().getSchoolPeriod();

        List<User> students = repos.findByPerson_StudentProfile_schoolPeriod(schoolPeriod);
        List<StudentUserResponseDto> dtos = new ArrayList<>();
        for (User student : students) {
            dtos.add(StudentMapper.toResponseDto(student));
        }
        return dtos;
    }

    @Transactional
    public StudentUserResponseDto createUser(StudentUserRequestDto dto) {
        User newStudent = StudentMapper.toEntity(dto);

        newStudent.setPassword(passwordEncoder.encode(dto.getPassword()));

        repos.save(newStudent);
        return StudentMapper.toResponseDto(newStudent);
    }

    @Transactional
    public StudentUserResponseDto updateEntity(Long id, StudentUserRequestDto dto) {
        User existingStudent = findStudent(id);
        StudentMapper.updateEntity(existingStudent, dto);
        repos.save(existingStudent);
        return StudentMapper.toResponseDto(existingStudent);
    }

    public void deleteUser(Long id) {
        User existingStudent = findStudent(id);
        repos.delete(existingStudent);
    }

}


