package com.projects.edustore.mapper;

import com.projects.edustore.dto.profile.CustomerUserRequestDto;
import com.projects.edustore.dto.profile.CustomerUserResponseDto;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.CustomerProfile;
import com.projects.edustore.model.person.Person;
import com.projects.edustore.model.Role;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public static User toEntity(CustomerUserRequestDto dto) {
        User user = new User();
        user.setRole(Role.ROLE_CUSTOMER);
        user.setUserName(dto.getUserName());

        Person person = Person.create(user, dto.getFirstName(), dto.getLastName(), dto.getEmail());
        CustomerProfile.create(person, dto.getPhoneNumber());

        return user;
    }

    public static void updateEntity(User existing, CustomerUserRequestDto dto) {
        Person person = existing.getPerson();
        //user
        if (dto.getUserName() != null) existing.setUserName(dto.getUserName());
        //person
        if (dto.getFirstName() != null) person.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) person.setLastName(dto.getLastName());
        if (dto.getEmail() != null) person.setEmail(dto.getEmail());
        //profile
        if (dto.getPhoneNumber() != null && person.getCustomerProfile() != null) {
            person.getCustomerProfile().setPhoneNumber(dto.getPhoneNumber());
        }
    }

    public static CustomerUserResponseDto toResponseDto(User user) {
        CustomerUserResponseDto dto = new CustomerUserResponseDto();

        Person person = user.getPerson();
        dto.setRole(person.getUser().getRole());
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());

        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setEmail(person.getEmail());

        if (person.getCustomerProfile() != null) {
            dto.setPhoneNumber(person.getCustomerProfile().getPhoneNumber());
        }
        return dto;
    }
}
