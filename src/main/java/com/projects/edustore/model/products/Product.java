//package com.projects.edustore.model.products;
//
//
//import com.projects.edustore.model.person.StudentProfile;
//import jakarta.persistence.*;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "products")
//public class Product {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String productName;
//    private String description;
//    private int stockQuantity;
//    private BigDecimal price;
//
//    @ManyToOne
//    @JoinColumn(name = "student_id")
//    private StudentProfile maker;
//
//    @OneToMany(mappedBy = "product")
//    private List<OrderItem> orderItems = new ArrayList<>();
//
//
//
//
//}
