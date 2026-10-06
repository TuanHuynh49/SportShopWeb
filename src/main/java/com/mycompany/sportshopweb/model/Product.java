package com.mycompany.sportshopweb.model;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name="Products")
public class Product implements Serializable {
    
    @Id
    @Column(name="productId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int productId;
    
    @Column(name="productName")
    private String productName;
    
    @Column(name="description")
    private String description;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandId", referencedColumnName = "brandId")
    private Brand brand;      
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId")
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