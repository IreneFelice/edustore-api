package com.projects.edustore.repository;

import com.projects.edustore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<User, Long> {

    }
