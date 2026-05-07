package com.projects.edustore.model.product.journey;

import com.projects.edustore.model.person.CustomerProfile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private CustomerProfile customer;

    protected Cart() {
    }

    public Cart(CustomerProfile customer) {
        this.customer = customer;
    }

    //getters + setters

    public void addCartItem(CartItem item) {
        cartItems.add(item);
        item.setCart(this);
    }

    public void removeCartItem(CartItem item) {
        cartItems.remove(item);
        item.setCart(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public CustomerProfile getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerProfile customer) {
        this.customer = customer;
        if (customer != null && customer.getCart() != this) {
            customer.setCart(this);
        }

    }
}

