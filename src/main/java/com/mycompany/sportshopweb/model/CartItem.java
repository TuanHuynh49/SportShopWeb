package com.mycompany.sportshopweb.model;
import com.mycompany.sportshopweb.model.ProductVariant;


public class CartItem {

    private ProductVariant productVariant;
    private int quantity;

    public CartItem() {
        this.quantity = 1;
    }

    public CartItem(ProductVariant productVariant, int quantity) {
        this.productVariant = productVariant;
        this.quantity = quantity;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}