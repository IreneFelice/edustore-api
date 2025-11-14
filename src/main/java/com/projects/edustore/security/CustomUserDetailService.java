package com.projects.edustore.security;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import com.projects.edustore.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository repos;

    public CustomUserDetailService(UserRepository repos) {
        this.repos = repos;
    }


    @Override
    public UserDetails loadUserByUsername(String userName) {
        User user = repos.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", userName));

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().name())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                authorities
        );
    }

}
