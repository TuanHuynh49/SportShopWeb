/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;
import java.io.Serializable;
import java.util.List;
public class Product {
    private int productId;
    private String productName;
    private String description;
    
    private List<ProductImage> images; 
    private Brand brand;               
    private Category category;         

    public Product() {
    }

    public Product(int productId, String productName, String description, List<ProductImage> images, Brand brand, Category category) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.images = images;
        this.brand = brand;
        this.category = category;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<ProductImage> getImages() { return images; }
    public void setImages(List<ProductImage> images) { this.images = images; }
    
    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}