package com.projects.edustore.model.person;

import com.projects.edustore.model.products.Product;
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
    private String schoolPeriod;

    @OneToMany(mappedBy = "maker")
    private List<Product> products = new ArrayList<>();

//    constructors

    public StudentProfile() {}

    protected StudentProfile(Person person, String schoolPeriod) {
        this.person = person;
        this.schoolPeriod = schoolPeriod;
    }

    public static StudentProfile create(Person person, String schoolPeriod) {
        StudentProfile profile = new StudentProfile(person, schoolPeriod);
        person.setStudentProfile(profile);
        return profile;
    }

    //    getters and setters

    public List<Product> getProducts() { //TODO
        return products;
    }
    public void addProduct(Product product) { //TODO
        products.add(product);
        product.setMaker(this);
    }
    public void removeProduct(Product product) { //TODO
        products.remove(product);
        product.setMaker(null);
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
