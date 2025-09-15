package com.projects.edustore.service;

import com.projects.edustore.dto.UserRequestDto;
import com.projects.edustore.mapper.UserMapper;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repos;

    public UserService(UserRepository repos){
        this.repos = repos;
    }

    public List<User> getAllUsers() {
        return this.repos.findAll();
    }

    public User createUser(UserRequestDto userRequestDto){
        return this.repos.save(UserMapper.toEntity(userRequestDto));
    }


}
