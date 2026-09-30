/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;
import java.io.Serializable;
import java.time.LocalDateTime;

public class ProductImage implements Serializable{
    private int productImageId;
    private String imageUrl;
    private boolean isPrimary;
    private int sortOrder;
    private LocalDateTime createdAt;

    public ProductImage() {
    }

    public ProductImage(int productImageId, String imageUrl, boolean isPrimary, int sortOrder, LocalDateTime createdAt) {
        this.productImageId = productImageId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getProductImageId() { return productImageId; }
    public void setProductImageId(int productImageId) { this.productImageId = productImageId; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }
    
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}