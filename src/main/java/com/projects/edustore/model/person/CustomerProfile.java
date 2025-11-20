package com.projects.edustore.model.person;
//import com.projects.edustore.model.products.Order;
import jakarta.persistence.*;

//import java.util.ArrayList;
//import java.util.List;

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

//    @OneToMany(mappedBy = "customer")
//    private List<Order> orderList = new ArrayList<>();

//    constructors

    protected CustomerProfile() {
    }

    protected CustomerProfile(Person person, String phoneNumber) {  //, List<Order> orderList
        this.person = person;
        this.phoneNumber = phoneNumber;
//        this.orderList = orderList;
    }

    public static CustomerProfile create(Person person, String phoneNumber) {
        CustomerProfile profile = new CustomerProfile(person, phoneNumber);
        person.setCustomerProfile(profile);
        return profile;
    }

//    getters and setters

//    public List<Order> getOrderList() {
//        return orderList;
//    }
//
//    public void setOrders(List<Order> orderList) {
//        this.orderList = orderList;
//    }

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

