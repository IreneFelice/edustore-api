package com.projects.edustore.service;

import com.projects.edustore.dto.profile.CustomerUserRequestDto;
import com.projects.edustore.dto.profile.CustomerUserResponseDto;
import com.projects.edustore.exception.EmailAlreadyExistsException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.exception.UserNameAlreadyExistsException;
import com.projects.edustore.mapper.CustomerMapper;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {
    private final UserRepository repos;
    private final PasswordEncoder passwordEncoder;
    private final AuthorisationService whoCanSee;

    public CustomerService(UserRepository repos, PasswordEncoder passwordEncoder, AuthorisationService whoCanSee) {
        this.repos = repos;
        this.passwordEncoder = passwordEncoder;
        this.whoCanSee = whoCanSee;
    }

    public User findCustomer(Long id) {
        return whoCanSee.findUserAndCheckAuthorisation(id).orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }

    public CustomerUserResponseDto getCustomerById(Long id) {
        User user = findCustomer(id);
        return CustomerMapper.toResponseDto(user);
    }

    private void validateNewUser(CustomerUserRequestDto dto){

        if(repos.existsByUserName(dto.getUserName())){
            throw new UserNameAlreadyExistsException();
        }

        if(repos.existsByPerson_Email(dto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
    }

    @Transactional
    public CustomerUserResponseDto createUser(CustomerUserRequestDto dto) {
        validateNewUser(dto);
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User newCustomer = CustomerMapper.toEntity(dto);
        newCustomer.setPassword(encodedPassword);

        repos.save(newCustomer);
        return CustomerMapper.toResponseDto(newCustomer);
    }

    @Transactional
    public CustomerUserResponseDto updateCustomer(Long id, CustomerUserRequestDto dto) {
        User existingCustomer = findCustomer(id);

        CustomerMapper.updateEntity(existingCustomer, dto);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingCustomer.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        repos.save(existingCustomer);
        return CustomerMapper.toResponseDto(existingCustomer);
    }

    public void deleteUser(Long id) {
        User existing = findCustomer(id);
        repos.delete(existing);
    }

}


