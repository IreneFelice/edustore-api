//package com.projects.edustore.model.products;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name="orderItems")
//public class OrderItem {
//    @Id
//    @GeneratedValue (strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name="order_id")
//    private Order order;
//
//    @ManyToOne
//    @JoinColumn(name="product_id")
//    private Product product;
//
//    private int quantity;
//
//}