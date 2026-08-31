package com.sportshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Category — Danh mục sản phẩm (Hỗ trợ danh mục đa cấp / Subcategories)
 */
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String slug;
    private String icon;
    private String description;
    private Long parentId;
    private Integer displayOrder;
    private Boolean active;
    private List<Category> subCategories = new ArrayList<>();

    public Category() {
        this.active = true;
        this.displayOrder = 0;
    }

    public Category(Long id, String name, String slug, String icon, Long parentId) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.icon = icon;
        this.parentId = parentId;
        this.active = true;
        this.displayOrder = 0;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public void addSubCategory(Category subCategory) {
        if (this.subCategories == null) {
            this.subCategories = new ArrayList<>();
        }
        this.subCategories.add(subCategory);
    }
}
