package com.projects.edustore.service;

import com.projects.edustore.dto.profileDto.StudentUserRequestDto;
import com.projects.edustore.dto.profileDto.StudentUserResponseDto;
import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.StudentMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public User getUserByUserName(String userName) {
        return repos.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", userName));
    }


    public User findStudent(Long id) {
        // get logged-in userName from SecurityContext
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        // get full (current) user
        User currentUser = getUserByUserName(userName);
        boolean isStudent = currentUser.getPerson() != null &&
                "StudentProfile".equals(currentUser.getPerson().getProfileLabel());

        // forbidden: if not admin and requests other user id
        if (currentUser.getRole() != Role.ROLE_ADMIN && !id.equals(currentUser.getId())) {
            throw new ForbiddenActionException("You are not allowed to access this user's information");
        }

        // Admin request by student id
        if (currentUser.getRole() == Role.ROLE_ADMIN) {

            return repos.findByIdAndPerson_ProfileLabel(id, "StudentProfile")
                    .orElseThrow(() -> new ResourceNotFoundException("Student", id));
        }

        // Student request by own id
        if (isStudent) {
            return currentUser;
        } else {
            throw new ResourceNotFoundException("Student", id);
        }
    }


    public StudentUserResponseDto getStudentById(Long id) {
        User user = findStudent(id);
        return StudentMapper.toResponseDto(user);
    }



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
        String hashed = passwordEncoder.encode(dto.getPassword());

        User newStudent = StudentMapper.toEntity(dto);
        newStudent.setPassword(hashed);

        repos.save(newStudent);
        return StudentMapper.toResponseDto(newStudent);
    }

    @Transactional
    public StudentUserResponseDto updateEntity(Long id, StudentUserRequestDto dto) {
        User existingStudent = findStudent(id);

        StudentMapper.updateEntity(existingStudent, dto);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingStudent.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        repos.save(existingStudent);
        return StudentMapper.toResponseDto(existingStudent);
    }

    public void deleteUser(Long id) {
        User existingStudent = findStudent(id);
        repos.delete(existingStudent);
    }

}


