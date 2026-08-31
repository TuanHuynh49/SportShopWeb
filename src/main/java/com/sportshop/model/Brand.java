package com.sportshop.model;

import java.io.Serializable;

/**
 * Brand — Thương hiệu sản phẩm (Nike, Adidas, Puma, Mizuno, v.v.)
 */
public class Brand implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String slug;
    private String logoUrl;
    private String description;
    private Boolean active;

    public Brand() {
        this.active = true;
    }

    public Brand(Long id, String name, String slug, String logoUrl) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.logoUrl = logoUrl;
        this.active = true;
    }

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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
