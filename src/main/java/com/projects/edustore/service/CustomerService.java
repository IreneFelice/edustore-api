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

    public User getUserByUsername(String username) {
        return repos.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
    }

    public User findCustomer(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = getUserByUsername(username);
        boolean isCustomer = currentUser.getPerson().getProfileLabel().equals("CustomerProfile");

        // if role is not admin and id does not match: forbidden
        if (currentUser.getRole() != Role.ROLE_ADMIN && !id.equals(currentUser.getId())) {
            throw new ForbiddenActionException("You are not allowed to access this user's information");
        }
        // if role is admin and id belongs to customer: return user from repos
        if (currentUser.getRole() == Role.ROLE_ADMIN) {

            return repos.findByIdAndPerson_ProfileLabel(id, "CustomerProfile")
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        }
        // if id does match and belongs to customer: return currentUser
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
        User newCustomer = CustomerMapper.toEntity(dto);

        newCustomer.setPassword(passwordEncoder.encode(dto.getPassword()));

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


