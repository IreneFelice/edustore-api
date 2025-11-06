package com.projects.edustore.service;

import com.projects.edustore.dto.profileDto.CustomerUserRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;

import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.CustomerMapper;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {
    private final CustomerRepository repos;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository repos, PasswordEncoder passwordEncoder) {
        this.repos = repos;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUser(Long id) {
        return repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer " + id + " not found."));
    }

    public CustomerUserResponseDto getCustomerById(Long id) {
        User user = findUser(id);
        return CustomerMapper.toResponseDto(user);
    }

    @Transactional
    public CustomerUserResponseDto createUser(CustomerUserRequestDto dto) {
        User newCustomer = CustomerMapper.toEntity(dto);

        newCustomer.setPassword(passwordEncoder.encode(dto.getPassword()));

        repos.save(newCustomer);
        return CustomerMapper.toResponseDto(newCustomer);
    }

    @Transactional
    public CustomerUserResponseDto updateCustomer(Long id, CustomerUserRequestDto dto) {
        User existingCustomer = findUser(id);
        if (existingCustomer.getPerson().getCustomerProfile() != null) {
            CustomerMapper.updateEntity(existingCustomer, dto);
        }
        repos.save(existingCustomer);
        return CustomerMapper.toResponseDto(existingCustomer);
    }

    public void deleteUser(Long id) {
        User existing = repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer" + id + "not found."));
        repos.delete(existing);
    }


}


