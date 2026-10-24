package com.mycompany.sportshopweb.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(
    name = "CartItem",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"customer_id", "variant_id"}
        )
    }
)
public class CartItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant productVariant;

    private int quantity = 1;

    public CartItem() {
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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