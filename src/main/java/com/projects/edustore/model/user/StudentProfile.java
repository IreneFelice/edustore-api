package com.projects.edustore.model.user;

import jakarta.persistence.*;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //    special fields for students
    private String schoolPeriod;


//    constructors

    public StudentProfile() {}

    public StudentProfile(User user, String schoolPeriod) {
        this.user = user;
        user.setStudentProfile(this);
        this.schoolPeriod = schoolPeriod;
    }

//    getters and setters

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    void setUser(User user) {
        this.user = user;
    }

    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }

}
