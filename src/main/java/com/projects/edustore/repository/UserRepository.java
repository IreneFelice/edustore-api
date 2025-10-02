package com.projects.edustore.repository;

import com.projects.edustore.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByProfileLabel(String profile_label);
    List<User> findByStudentProfile_schoolPeriodIgnoreCase(String schoolPeriod);

    Optional<User> findByEmail(String email);
}
