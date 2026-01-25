package com.projects.edustore.service;

import com.projects.edustore.dto.profileDto.CustomerUserRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;
import com.projects.edustore.mapper.CustomerMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {
    private final CustomerRepository repos;
    private final PasswordEncoder passwordEncoder;
    private final WhoCanSeeWhoService whoCanSee;

    public CustomerService(CustomerRepository repos, PasswordEncoder passwordEncoder, WhoCanSeeWhoService whoCanSee) {
        this.repos = repos;
        this.passwordEncoder = passwordEncoder;
        this.whoCanSee = whoCanSee;
    }

    public User findCustomer(Long id) {
        return whoCanSee.authorizeUserAccess(id, Role.ROLE_CUSTOMER, "Customer");
    }

    public CustomerUserResponseDto getCustomerById(Long id) {
        User user = findCustomer(id);
        return CustomerMapper.toResponseDto(user);
    }

    @Transactional
    public CustomerUserResponseDto createUser(CustomerUserRequestDto dto) {
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


