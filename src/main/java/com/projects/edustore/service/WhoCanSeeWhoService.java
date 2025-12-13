package com.projects.edustore.service;

import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
// Search user with given id, is the current user allowed to get info and check if their role matches the expected role.
// Example: Does user with id 52 exist, is this also the current user OR an admin and is it indeed a student?
// Return appropriate message.
public class WhoCanSeeWhoService {

    private final UserRepository repos;

    public WhoCanSeeWhoService(UserRepository repos) {
        this.repos = repos;
    }

    public User getSearchedUser(Long id, Role expectedRole, String roleName) {
        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = repos.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", userName));

        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        boolean isSelf = currentUser.getId().equals(id);

        // forbidden: is not admin and requests other user id
        if (!isAdmin && !isSelf) {
            throw new ForbiddenActionException(
                    "You are not allowed to access this user's information"
            );
        }

        //user with this id could be found (or not)
        User searchedUser = repos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(roleName, id));

        //found user does not have expected role
        if (expectedRole != null && searchedUser.getRole() != expectedRole) {
            throw new ResourceNotFoundException(roleName, id);
        }
        //found user with expected role does exist
        return searchedUser;
    }
}
