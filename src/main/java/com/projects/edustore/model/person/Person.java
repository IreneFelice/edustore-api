package com.projects.edustore.model.person;

import com.projects.edustore.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private StudentProfile studentProfile;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private CustomerProfile customerProfile;


    //    constructors

    protected Person() {
    }

    protected Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email.toLowerCase();
    }

    public static Person create(User user, String firstName, String lastName, String email) {
        Person person = new Person(firstName, lastName, email);
        person.setUser(user);
        return person;
    }


    //    getters and setters


    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public User getUser() {
        return user;
    }

    public StudentProfile getStudentProfile() {
        return studentProfile;
    }

    public CustomerProfile getCustomerProfile() {
        return customerProfile;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null && user.getPerson() != this) {
            user.setPerson(this);
        }
    }

    public void setStudentProfile(StudentProfile studentProfile) {
        if (studentProfile != null && this.customerProfile != null) {
            throw new IllegalStateException("Person cannot be both student and customer");
        }
        this.studentProfile = studentProfile;

        if (studentProfile != null && studentProfile.getPerson() != this) {
            studentProfile.setPerson(this);
        }
    }

    public void setCustomerProfile(CustomerProfile customerProfile) {
        if (customerProfile != null && this.studentProfile != null) {
            throw new IllegalStateException("Person cannot be both student and customer");
        }
        this.customerProfile = customerProfile;

        if (customerProfile != null && customerProfile.getPerson() != this)
            customerProfile.setPerson(this);
    }
}


