package com.projects.edustore.model.person;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_profiles")
public class AdminProfile {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Person person;

    public AdminProfile() {
    }

    public AdminProfile(Person person) {
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person; }

    protected void setPerson(Person person) {
        this.person = person;
    }

}
