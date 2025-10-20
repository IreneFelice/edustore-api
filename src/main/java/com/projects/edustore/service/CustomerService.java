package com.projects.edustore.service;

import com.projects.edustore.dto.adminDto.AdminRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;

import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.CustomerMapper;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.CustomerRepository;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {
    private final CustomerRepository repos;

    public CustomerService(CustomerRepository repos) {
        this.repos = repos;
    }

    public User findUser(Long id) {
        return repos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer " + id + " not found."));
    }

    public CustomerUserResponseDto getCustomerById(Long id) {
        User user = findUser(id);
        return CustomerMapper.toResponseDto(user);
    }

    public CustomerUserResponseDto createUser(CustomerUserRequestDto customerUserRequestDto) {
        User newCustomer = CustomerMapper.toEntity(customerUserRequestDto);
        repos.save(newCustomer);
        return CustomerMapper.toResponseDto(newCustomer);
    }

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

    /////// used by Admin from UserService
    public void attachProfile(User user, AdminRequestDto dto) {
        CustomerMapper.applyCustomerData(user.getPerson(), dto);
    }

}


