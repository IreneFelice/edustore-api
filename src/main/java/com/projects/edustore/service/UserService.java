package com.projects.edustore.service;

import com.projects.edustore.dto.UserRequestDto;
import com.projects.edustore.mapper.UserMapper;
import com.projects.edustore.model.user.User;
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

    public User getUserById(Long id) {
        return repos.findById(id).orElse(null);
    }

    public User updateUser(Long id, UserRequestDto userRequestDto) {
        User existing = getUserById(id);
        UserMapper.updateEntity(existing, userRequestDto);
        return repos.save(existing);
    }

    public void deleteUser(Long id) {
        User existing = getUserById(id);
        repos.delete(existing);
    }
}
