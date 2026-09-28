/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;
import java.io.Serializable;

/**
 *
 * @author Anh Tuan
 */
public class Adress implements Serializable{
    
    private String addressId;
    private String province;
    private String ward;
    private String detailAddress;
    private boolean isDefault;

    public Adress() {
        this.addressId = "";
        this.province = "";
        this.ward = "";
        this.detailAddress = "";
        this.isDefault = false;
    }

    public Adress(String addressId, String province, String ward,
                  String detailAddress, boolean isDefault) {
        this.addressId = addressId;
        this.province = province;
        this.ward = ward;
        this.detailAddress = detailAddress;
        this.isDefault = isDefault;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
