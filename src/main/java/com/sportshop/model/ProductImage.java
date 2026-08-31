package com.sportshop.model;

import java.io.Serializable;

/**
 * ProductImage — Hình ảnh của sản phẩm
 */
public class ProductImage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long productId;
    private String imageUrl;
    private Boolean isPrimary;
    private Integer displayOrder;

    public ProductImage() {
        this.isPrimary = false;
        this.displayOrder = 0;
    }

    public ProductImage(Long id, Long productId, String imageUrl, Boolean isPrimary) {
        this.id = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary != null ? isPrimary : false;
        this.displayOrder = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
