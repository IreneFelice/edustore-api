package com.projects.edustore.model.person;

import com.projects.edustore.model.product.journey.Cart;
import com.projects.edustore.model.product.journey.Order;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_profiles")
public class CustomerProfile {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Person person;

    private String phoneNumber;

    @OneToMany(mappedBy = "customer")
    private final List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;


//    constructors

    protected CustomerProfile() {
    }

    protected CustomerProfile(Person person, String phoneNumber) {
        this.person = person;
        this.phoneNumber = phoneNumber;
    }

    public static CustomerProfile create(Person person, String phoneNumber) {
        CustomerProfile profile = new CustomerProfile(person, phoneNumber);
        person.setCustomerProfile(profile);
        return profile;
    }

//    getters and setters

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    protected void setPerson(Person person) {
        this.person = person;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        if (cart != null && cart.getCustomer() != this) {
            cart.setCustomer(this);
        }
    }
}