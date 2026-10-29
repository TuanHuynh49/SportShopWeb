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
@Table(name = "ProductVariants")
public class ProductVariant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "variant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int variantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name = "variant_name")
    private String variantName;

    @Column(name = "size")
    private String size;

    @Column(name = "color")
    private String color;

    @Column(name = "price")
    private long price;

    @Column(name = "quantity")
    private int quantity;

    public ProductVariant() {
    }

    // Giữ constructor cũ để hạn chế ảnh hưởng đến code đang sử dụng.
    public ProductVariant(
            int variantId,
            Product product,
            String variantName,
            String size,
            String color,
            long price) {

        this.variantId = variantId;
        this.product = product;
        this.variantName = variantName;
        this.size = size;
        this.color = color;
        this.price = price;
    }

    // Constructor có thêm quantity để khởi tạo đầy đủ variant.
    public ProductVariant(
            int variantId,
            Product product,
            String variantName,
            String size,
            String color,
            long price,
            int quantity) {

        this(variantId, product, variantName, size, color, price);
        setQuantity(quantity);
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }
}
