package com.projects.edustore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_profiles")
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

//    special fields for customers
    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private Boolean isParent;

//    constructors

    public CustomerProfile() {}

    public CustomerProfile(User user, String phoneNumber, Boolean isParent) {
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.isParent = isParent;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }
}
