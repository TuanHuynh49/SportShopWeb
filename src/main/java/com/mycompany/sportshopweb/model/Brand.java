/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;

/**
 *
 * @author dung2
 */
public class Brand {
    private int brandId;
    private String name;
    private String logoUrl;
    private String description;

    public Brand() {
    }

    public Brand(int brandId, String name, String logoUrl, String description) {
        this.brandId = brandId;
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
    }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}