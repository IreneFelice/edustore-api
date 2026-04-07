package com.projects.edustore.repository;

import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByPerson_StudentProfile_TeamIn(List<String> teams);

    List<User> findByPerson_StudentProfile_Team(String team);
    Optional<User> findByPerson_Email(String email);
    Optional<User> findByUserName(String username);
    List<User> findByRole(Role role);
}
