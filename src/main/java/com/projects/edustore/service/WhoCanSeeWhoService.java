package com.projects.edustore.service;

import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service

public class WhoCanSeeWhoService {

    private final UserRepository repos;

    public WhoCanSeeWhoService(UserRepository repos) {
        this.repos = repos;
    }

    public User getCurrentUser() {

        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return repos.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", userName));
    }


    public void checkSelfOrAdminAccess(Long id) { //authorizeUserAccess

        User currentUser = getCurrentUser();

        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        boolean isSelf = currentUser.getId().equals(id);

        //  Not admin and trying to access another user
        if (!isAdmin && !isSelf) {
            throw new ForbiddenActionException(
                    "You are not allowed to access or modify this resource"
            );
        }
    }

    public Optional<User> findUserAndCheckAuthorisation(Long id) {
        checkSelfOrAdminAccess(id);
        return repos.findById(id);
    }

}


