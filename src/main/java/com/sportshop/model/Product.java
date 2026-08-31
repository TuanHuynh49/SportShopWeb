package com.sportshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Product — Mô hình sản phẩm thể thao
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String slug;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private BigDecimal price;
    private BigDecimal salePrice;
    private String thumbnailUrl;
    private String description;
    private Boolean featured;
    private Boolean isNew;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ProductVariant> variants = new ArrayList<>();
    private List<ProductImage> images = new ArrayList<>();

    public Product() {
        this.featured = false;
        this.isNew = true;
        this.active = true;
        this.price = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
    }

    public Product(Long id, String name, String slug, Long categoryId, Long brandId, BigDecimal price, BigDecimal salePrice, String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.price = price != null ? price : BigDecimal.ZERO;
        this.salePrice = salePrice;
        this.thumbnailUrl = thumbnailUrl;
        this.featured = false;
        this.isNew = true;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public boolean hasDiscount() {
        return salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0 && salePrice.compareTo(price) < 0;
    }

    public int getDiscountPercent() {
        if (!hasDiscount() || price.compareTo(BigDecimal.ZERO) == 0) return 0;
        BigDecimal diff = price.subtract(salePrice);
        return diff.multiply(new BigDecimal(100)).divide(price, 0, java.math.RoundingMode.HALF_UP).intValue();
    }

    public BigDecimal getEffectivePrice() {
        return hasDiscount() ? salePrice : price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }
}
