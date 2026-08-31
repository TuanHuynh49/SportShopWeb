package com.sportshop.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ProductVariant — Biến thể sản phẩm (Kích thước, Màu sắc, Tồn kho, Giá)
 */
public class ProductVariant implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long productId;
    private String sku;
    private String size;
    private String color;
    private BigDecimal price;
    private Integer stockQuantity;
    private Boolean active;

    public ProductVariant() {
        this.stockQuantity = 0;
        this.active = true;
    }

    public ProductVariant(Long id, Long productId, String sku, String size, String color, BigDecimal price, Integer stockQuantity) {
        this.id = id;
        this.productId = productId;
        this.sku = sku;
        this.size = size;
        this.color = color;
        this.price = price;
        this.stockQuantity = stockQuantity != null ? stockQuantity : 0;
        this.active = true;
    }

    public boolean isInStock() {
        return stockQuantity != null && stockQuantity > 0;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
