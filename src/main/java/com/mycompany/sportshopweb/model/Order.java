package com.mycompany.sportshopweb.model;

import java.util.ArrayList;
import java.util.List;
import com.mycompany.sportshopweb.model.Customer;

public class Order {

    private int orderId;
    private Customer customer;
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
    }

    // Getter & Setter cho orderId
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    // Getter & Setter cho customer
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // Getter cho orderItems
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
    
  
