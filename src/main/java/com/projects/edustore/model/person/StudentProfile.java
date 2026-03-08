package com.projects.edustore.model.person;

import com.projects.edustore.model.product.Product;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


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
    private String team;

    @OneToMany(mappedBy = "maker")
    private List<Product> products = new ArrayList<>();

//    constructors

    public StudentProfile() {
    }

    protected StudentProfile(Person person, String team) {
        this.person = person;
        this.team = team;
    }

    public static StudentProfile create(Person person, String team) {
        StudentProfile profile = new StudentProfile(person, team);
        person.setStudentProfile(profile);
        return profile;
    }

    //    getters and setters

    public List<Product> getProducts() { //TODO
        return products;
    }

    public Long getId() {
        return id;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team.toLowerCase();
    }

    public Person getPerson() {
        return person;
    }

    protected void setPerson(Person person) {
        this.person = person;
    }


}