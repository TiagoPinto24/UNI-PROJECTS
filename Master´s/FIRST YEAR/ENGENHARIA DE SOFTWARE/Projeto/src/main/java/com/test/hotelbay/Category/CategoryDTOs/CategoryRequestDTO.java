package com.test.hotelbay.Category.CategoryDTOs;

import java.util.List;

import com.test.hotelbay.Category.Category;

public class CategoryRequestDTO {
    private String name;
    private List<Category> subCategories;
    private Category superCategory;
 
    public CategoryRequestDTO() {}

    public CategoryRequestDTO(String name, Category superCategory, List<Category> subCategories) {
        this.name = name;
        this.superCategory = superCategory;
        this.subCategories = subCategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getSuperCategory() {
        return superCategory;
    }

    public void setSuperCategory(Category superCategory) {
        this.superCategory = superCategory;
    }

    public List<Category> getSubCategory() {
        return subCategories;
    }

    public void setSubCategory(List<Category> subCategory) {
        this.subCategories = subCategory;
    }
}
