package com.projects.edustore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

//    special fields for students
    private String schoolPeriod;

    @Column(nullable = false)
    private Integer maxHours; //    weekly, monthly or total?

    @Column(nullable = false)
    private Boolean onlyParents;

//    constructors

    public StudentProfile() {}

    public StudentProfile(User user, String schoolPeriod, Integer maxHours, Boolean onlyParents) {
        this.user = user;
        this.schoolPeriod = schoolPeriod;
        this.maxHours = maxHours;
        this.onlyParents = onlyParents;
    }

//    getters and setters

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }

    public Integer getMaxHours() {
        return maxHours;
    }

    public void setMaxHours(Integer maxHours) {
        this.maxHours = maxHours;
    }

    public Boolean getOnlyParents() {
        return onlyParents;
    }

    public void setOnlyParents(Boolean onlyParents) {
        this.onlyParents = onlyParents;
    }
}
