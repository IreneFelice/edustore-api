package com.projects.edustore.repository;

import com.projects.edustore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    Optional<User> findById(Long id);

    }
