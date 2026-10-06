/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java 
 */
package com.mycompany.sportshopweb.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="ProductImages")
public class ProductImage implements Serializable{
    @Id
    @Column(name="productId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int productImageId;
    @Column(name="imageUrl")
    private String imageUrl;
    @Column(name="isPrimary")
    private boolean isPrimary;
    @Column(name="sortOrder")
    private int sortOrder;
    @Column(name="LocalDateTime")
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