package com.projects.edustore.service;

import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
/*
  This service verifies:
  - who the currently authenticated user is
  - if they are allowed to access requested user (self or admin)
  - if the requested user exists
  - if the requested user has the expected role
*/

public class WhoCanSeeWhoService {

    private final UserRepository repos;

    public WhoCanSeeWhoService(UserRepository repos) {
        this.repos = repos;
    }

    public User authorizeUserAccess(Long id, Role expectedRole, String roleName) {

        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = repos.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", userName));


        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        boolean isSelf = currentUser.getId().equals(id);

        //  Not admin and trying to access another user
        if (!isAdmin && !isSelf) {
            throw new ForbiddenActionException(
                    "You are not allowed to access or alter this user's information"
            );
        }

        //find requested user
        User requestedUser = repos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(roleName, id));

        //requested user does not have expected role
        if (expectedRole != null && requestedUser.getRole() != expectedRole) {
            throw new ResourceNotFoundException(roleName, id);
        }
        //requested user with expected role does exist
        return requestedUser;
    }
}
