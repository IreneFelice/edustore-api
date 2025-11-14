package com.projects.edustore.model.person;


import jakarta.persistence.*;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Person person;

    //    special fields for students
    private String schoolPeriod;


//    constructors

    public StudentProfile() {}

    public StudentProfile(Person person, String schoolPeriod) {
        this.person = person;
        this.schoolPeriod = schoolPeriod;
    }

    public Long getId() {
        return id;
    }

    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }
    public Person getPerson() {
        return person; }

    protected void setPerson(Person person) {
        this.person = person;
    }


}
