/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;

import java.io.Serializable;
import java.util.List;

public class Review implements Serializable{
    private int reviewId; // Tương ứng với review_id: int PK[cite: 2]
    private Customer customer; // Tương ứng với customer: Customer[cite: 2]
    private ProductVariant productVariant; // Tương ứng với productvariant: ProductVariant[cite: 2]
    private int rating; // Tương ứng với rating: int[cite: 2]
    private String comment; // Tương ứng với comment: string[cite: 2]
    private List<String> images; // Tương ứng với images: List<string>[cite: 2]

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