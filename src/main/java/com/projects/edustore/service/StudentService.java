package com.projects.edustore.service;

import com.projects.edustore.dto.adminDto.AdminRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.StudentMapper;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class StudentService {
    private final StudentRepository repos;

    public StudentService(StudentRepository repos) {
        this.repos = repos;
    }

    public User findUser(Long id) {
        return repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student" + id + "not found."));
    }

//    public List<StudentUserResponseDto> getAllStudents() {
//        List<User> students = repos.findByPerson_ProfileLabel("StudentProfile");
//        List<StudentUserResponseDto> dtos = new ArrayList<>();
//        for (User user : students) {
//            dtos.add(StudentMapper.toResponseDto(user));
//        }
//        return dtos;
//    }

    public StudentUserResponseDto getStudentById(Long id) {
        User user = findUser(id);
        return StudentMapper.toResponseDto(user);
    }

    public List<StudentUserResponseDto> getBySchoolPeriod(Long id) {
        User user = findUser(id);
        String schoolPeriod = user.getPerson().getStudentProfile().getSchoolPeriod();

        List<User> students = repos.findByPerson_StudentProfile_schoolPeriod(schoolPeriod);
        List<StudentUserResponseDto> dtos = new ArrayList<>();
        for (User student : students) {
            dtos.add(StudentMapper.toResponseDto(student));
        }
        return dtos;
    }

    public StudentUserResponseDto createUser(StudentUserRequestDto dto) {
        User newStudent = StudentMapper.toEntity(dto);
        repos.save(newStudent);
        return StudentMapper.toResponseDto(newStudent);
    }

    public StudentUserResponseDto updateEntity(Long id, StudentUserRequestDto dto) {
        User existingStudent = findUser(id);
        StudentMapper.updateEntity(existingStudent, dto);
        repos.save(existingStudent);
        return StudentMapper.toResponseDto(existingStudent);
    }

    public void deleteUser(Long id) {
        User existing = repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer" + id + "not found."));
        repos.delete(existing);
    }

    /////// used by Admin from UserService
    public void attachProfile(User user, AdminRequestDto dto) {
        StudentMapper.applyStudentData(user.getPerson(), dto);
    }

}


