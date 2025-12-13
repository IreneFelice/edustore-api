package com.projects.edustore.repository;

import com.projects.edustore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<User, Long> {


    List<User> findByPerson_StudentProfile_schoolPeriod(String schoolPeriod);

}
