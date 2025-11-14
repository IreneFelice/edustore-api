package com.projects.edustore.service;

import com.projects.edustore.dto.profileDto.CustomerUserRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;
import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.CustomerMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public User getUserByUserName(String userName) {
        return repos.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", userName));
    }

    public User findCustomer(Long id) {
        // get logged-in userName from SecurityContext
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        // get full (current) user
        User currentUser = getUserByUserName(userName);
        boolean isCustomer = currentUser.getPerson() != null &&
                "CustomerProfile".equals(currentUser.getPerson().getProfileLabel());

        // forbidden: if not admin and requests other user id
        if (currentUser.getRole() != Role.ROLE_ADMIN && !id.equals(currentUser.getId())) {
            throw new ForbiddenActionException("You are not allowed to access this user's information");
        }

        // Admin request by customer id
        if (currentUser.getRole() == Role.ROLE_ADMIN) {

            return repos.findByIdAndPerson_ProfileLabel(id, "CustomerProfile")
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        }

        // Customer request by own id
        if (isCustomer) {
            return currentUser;
        } else {
            throw new ResourceNotFoundException("Customer", id);
        }
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


