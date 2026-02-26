package com.projects.edustore.model.products;


import com.projects.edustore.model.person.StudentProfile;
import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;

    private Integer stockQuantity;
    private BigDecimal costPrice;

    @ManyToOne
    @JoinColumn(name = "maker_id")
    private StudentProfile maker;


    @Lob
    private byte[] bytes;
    private String contentType;
    private String originalFilename;


    //    Constructors
    public Product() {
    }

    public void addImage(byte[] bytes, String contentType, String originalFilename) {
        this.bytes = bytes;
        this.contentType = contentType; //MIME type
        this.originalFilename = originalFilename;
    }

    public void removeImage() {
        this.bytes = null;
        this.contentType = null;
        this.originalFilename = null;
    }

//    Getters & setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public StudentProfile getMaker() {
        return maker;
    }

    public void setMaker(StudentProfile maker) {
        this.maker = maker;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }
}
