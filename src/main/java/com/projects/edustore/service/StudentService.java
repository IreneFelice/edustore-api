package com.projects.edustore.service;

import com.projects.edustore.dto.profile.StudentUserRequestDto;
import com.projects.edustore.dto.profile.StudentUserResponseDto;
import com.projects.edustore.exception.EmailAlreadyExistsException;
import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.exception.UserNameAlreadyExistsException;
import com.projects.edustore.mapper.StudentMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.product.Product;
import com.projects.edustore.repository.ProductRepository;
import com.projects.edustore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class StudentService {

    private final UserRepository repos;
    private final PasswordEncoder passwordEncoder;
    private final AuthorisationService whoCanSee;
    private final ProductRepository productRepos;

    public StudentService(UserRepository repos, PasswordEncoder passwordEncoder, AuthorisationService whoCanSee, ProductRepository productRepos) {
        this.repos = repos;
        this.passwordEncoder = passwordEncoder;
        this.whoCanSee = whoCanSee;
        this.productRepos = productRepos;
    }

    private User findAndAuthorizeStudent(Long id) {
        return whoCanSee.findUserAndCheckAuthorisation(id).orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }

    public StudentUserResponseDto getStudentById(Long id) {
        User user = findAndAuthorizeStudent(id);
        if (user.getRole().equals(Role.ROLE_STUDENT)) {
            return StudentMapper.toResponseDto(user);
        } else {
            throw new ResourceNotFoundException("Student", id);
        }
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
    public StudentUserResponseDto createStudentUser(StudentUserRequestDto dto) {
        validateNewUser(dto);
        String hashed = passwordEncoder.encode(dto.getPassword());

        User newStudent = StudentMapper.toEntity(dto);
        newStudent.setPassword(hashed);

        User savedUser = repos.save(newStudent);

        return StudentMapper.toResponseDto(savedUser);
    }

    private void validateNewUser(StudentUserRequestDto dto){

        if(repos.existsByUserName(dto.getUserName())){
            throw new UserNameAlreadyExistsException();
        }

        if(repos.existsByPerson_Email(dto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
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

        List<Product> ownedProducts = productRepos.findByMaker_Id(id);
        if (ownedProducts.size() > 0) {
            throw new ForbiddenActionException(
                    "Student still has " + ownedProducts.size() + " products and cannot be deleted"
            );
        }
        repos.delete(existingStudent);
    }

}


