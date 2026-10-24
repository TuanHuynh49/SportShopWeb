/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Anh Tuan
 */
@Entity
@Table(name = "Addresses")
public class Adress implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int addressId;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String province;
    private String ward;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column(name = "deleted")
    private boolean deleted;

    public Adress() {
        this.addressId = 0;
        this.customer = new Customer();
        this.province = "";
        this.ward = "";
        this.detailAddress = "";
        this.isDefault = false;
        this.deleted = false;
    }

    public Adress(int addressId, String province, String ward, String detailAddress, boolean isDefault) {
        this.addressId = addressId;
        this.customer = new Customer();
        this.province = province;
        this.ward = ward;
        this.detailAddress = detailAddress;
        this.isDefault = isDefault;
    }

    public Adress(int addressId, Customer customer, String province, String ward, String detailAddress, boolean isDefault) {
        this.addressId = addressId;
        this.customer = customer;
        this.province = province;
        this.ward = ward;
        this.detailAddress = detailAddress;
        this.isDefault = isDefault;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public boolean getDefaultAddress() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}


