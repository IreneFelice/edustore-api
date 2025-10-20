package com.projects.edustore.mapper;

import com.projects.edustore.dto.adminDto.AdminRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserRequestDto;
import com.projects.edustore.dto.profileDto.CustomerUserResponseDto;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.CustomerProfile;
import com.projects.edustore.model.person.Person;
import com.projects.edustore.model.Role;

public class CustomerMapper {

    public static User toEntity(CustomerUserRequestDto dto) {
        User user = new User();
        user.setRole(Role.ROLE_CUSTOMER);
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());

        Person person = new Person();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setEmail(dto.getEmail());
        person.setProfileLabel("CustomerProfile");

        user.setPerson(person);

        CustomerProfile profile = new CustomerProfile();
        profile.setPhoneNumber(dto.getPhoneNumber());

        person.setCustomerProfile(profile);

        return user;
    }

    public static void updateEntity(User existing, CustomerUserRequestDto dto) {
        Person person = existing.getPerson();
        //user
        if (dto.getUserName() != null) existing.setUserName(dto.getUserName());
        if (dto.getPassword() != null) existing.setPassword(dto.getPassword());
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

        dto.setId(user.getId());
        dto.setUserName(user.getUserName());

        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setEmail(person.getEmail());
        dto.setProfileLabel(person.getProfileLabel());

        if (person.getCustomerProfile() != null) {
            dto.setPhoneNumber(person.getCustomerProfile().getPhoneNumber());
        }
        return dto;
    }

    /////// used by Admin through UserService -> CustomerService
    public static void applyCustomerData(Person person, AdminRequestDto dto) {
        CustomerProfile profile = person.getCustomerProfile();
        if (profile == null) {
            profile = new CustomerProfile();
            person.setCustomerProfile(profile);
        }
        person.setProfileLabel("CustomerProfile");
        if (dto.getPhoneNumber() != null) {
            person.getCustomerProfile().setPhoneNumber(dto.getPhoneNumber());
        }
    }
}
