/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;

import java.io.Serializable;

public class ProductVariant implements Serializable{
    private int variantId;
    private int productId;
    private String variantName;
    private String size;
    private String color;
    private long price;
    private boolean status;

    public ProductVariant() {
    }

    public ProductVariant(int variantId, int productId, String variantName, String size, String color, long price, boolean status) {
        this.variantId = variantId;
        this.productId = productId;
        this.variantName = variantName;
        this.size = size;
        this.color = color;
        this.price = price;
        this.status = status;
    }

    public int getVariantId() { return variantId; }
    public void setVariantId(int variantId) { this.variantId = variantId; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getVariantName() { return variantName; }
    public void setVariantName(String variantName) { this.variantName = variantName; }
    
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }
    
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
