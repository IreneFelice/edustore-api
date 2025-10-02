package com.projects.edustore.model.user;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_profiles")
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    special fields for customers
    @Column(nullable = false)
    private String phoneNumber;



//    constructors

    public CustomerProfile() {}

    public CustomerProfile(User user, String phoneNumber) {
        this.user = user;
        user.setCustomerProfile(this);
        this.phoneNumber = phoneNumber;
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

    }

