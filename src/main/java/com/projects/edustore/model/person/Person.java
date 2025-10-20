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

    private String firstName;
    private String lastName;
    private String email;
    private String profileLabel;


    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private StudentProfile studentProfile;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private CustomerProfile customerProfile;

//    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
//    private AdminProfile adminProfile;

    //    constructors

    public Person() {
    }

    protected Person(String firstName, String lastName, String profileLabel, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileLabel = profileLabel;
        this.email = email;
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

    public String getProfileLabel() {
        return profileLabel;
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
        this.email = email;
    }

    public void setProfileLabel(String profileLabel) {
        this.profileLabel = profileLabel;
    }


    public void setUser(User user) {
        this.user = user;
        if (user != null && user.getPerson() != this) {
            user.setPerson(this);
        }
    }

    //    Connect profile to person
    public void setStudentProfile(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
        if (studentProfile != null && studentProfile.getPerson() != this) {
            studentProfile.setPerson(this);
        }
    }

    public void setCustomerProfile(CustomerProfile customerProfile) {
        this.customerProfile = customerProfile;
        if (customerProfile != null && customerProfile.getPerson() != this)
            customerProfile.setPerson(this);
    }

//    public void setAdminProfile(AdminProfile adminProfile) {
//        this.adminProfile = adminProfile;
//        if (adminProfile != null && adminProfile.getPerson() != this) {
//            adminProfile.setPerson(this);
//        }
//    }
}