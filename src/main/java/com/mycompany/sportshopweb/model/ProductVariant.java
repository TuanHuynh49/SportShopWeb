/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
@Entity
@Table(name="ProductVariants")
public class ProductVariant implements Serializable{
    @Id
    @Column(name="variantId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int variantId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private Product product;
    private String variantName;
    private String size;
    private String color;
    private long price;
    @Column(name = "isActive")
    private boolean isActive = true;

    public ProductVariant() {
    }

    public ProductVariant(int variantId, Product product, String variantName, String size, String color, long price) {
        this.variantId = variantId;
        this.product = product;
        this.variantName = variantName;
        this.size = size;
        this.color = color;
        this.price = price;
    }

    public int getVariantId() { return variantId; }
    public void setVariantId(int variantId) { this.variantId = variantId; }
    
    public Product getProduct() { return product; }
    public void setProductId(Product product) { this.product = product; }
    
    public String getVariantName() { return variantName; }
    public void setVariantName(String variantName) { this.variantName = variantName; }
    
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }
    
    public boolean isActive() { return isActive; }
    public void setisActive(boolean status) { this.isActive = status; }
}
