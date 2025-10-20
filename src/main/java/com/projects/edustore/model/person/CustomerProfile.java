package com.projects.edustore.model.person;
import jakarta.persistence.*;

@Entity
@Table(name = "customer_profiles")
public class CustomerProfile {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Person person;

    //    special fields for customers
    private String phoneNumber;


//    constructors

    public CustomerProfile() {
    }

    public CustomerProfile(Person person, String phoneNumber) {
        this.person = person;
        this.phoneNumber = phoneNumber;
    }

//    getters and setters

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person; }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    protected void setPerson(Person person) {
        this.person = person;
    }

}

