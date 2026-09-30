package com.mycompany.sportshopweb.model;

public class OrderItem {

    private ProductVariant item;
    private int quantity;

    public OrderItem() {
        this.quantity = 1;
    }

    public OrderItem(ProductVariant item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    // Getter & Setter cho item
    public ProductVariant getItem() {
        return item;
    }

    public void setItem(ProductVariant item) {
        this.item = item;
    }

    // Getter & Setter cho quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}