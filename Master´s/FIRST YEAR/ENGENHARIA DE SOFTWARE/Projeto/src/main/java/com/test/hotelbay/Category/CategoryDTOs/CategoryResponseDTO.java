package com.test.hotelbay.Category.CategoryDTOs;

import java.util.List;

public class CategoryResponseDTO {

    private String name;
    private Long adminId;
    private Long superCategoryId;
    private List<Long> subCategoryIds;

    public CategoryResponseDTO() {}

    public CategoryResponseDTO(String name, Long adminId) {
        this.name = name;
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getSuperCategoryId() {
        return superCategoryId;
    }

    public void setSuperCategoryId(Long superCategoryId) {
        this.superCategoryId = superCategoryId;
    }

    public List<Long> getSubCategoryIds() {
        return subCategoryIds;
    }

    public void setSubCategoryIds(List<Long> subCategoryIds) {
        this.subCategoryIds = subCategoryIds;
    }
}

