//package com.projects.edustore.model.products;
//
//import com.projects.edustore.model.person.CustomerProfile;
//import jakarta.persistence.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "orders")
//public class Order {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private CustomerProfile customer;
//
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
//    private List<OrderItem> items = new ArrayList<>();
//
////status
//    // product (n) -> hashmap
//    // date
//    // total price
//
//
//    public Order(CustomerProfile customer, List<OrderItem> items) {
//        this.customer = customer;
//        this.items = items;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public CustomerProfile getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(CustomerProfile customer) {
//        this.customer = customer;
//    }
//
//    public List<OrderItem> getItems() {
//        return items;
//    }
//
//    public void setItems(List<OrderItem> items) {
//        this.items = items;
//    }
//}