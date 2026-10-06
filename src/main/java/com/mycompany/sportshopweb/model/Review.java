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
import java.util.List;
@Entity
@Table(name="Review")
public class Review implements Serializable{
    @Id
    @Column(name="variantId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int reviewId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", referencedColumnName = "customerId")
    private Customer customer; 
    
    @JoinColumn(name = "variantId", referencedColumnName = "variantId")
    private ProductVariant productVariant;
    private int rating; 
    private String comment;
    private List<String> images; 

    public Review() {
    }

    public Review(int reviewId, Customer customer, ProductVariant productVariant, int rating, String comment, List<String> images) {
        this.reviewId = reviewId;
        this.customer = customer;
        this.productVariant = productVariant;
        this.rating = rating;
        this.comment = comment;
        this.images = images;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}