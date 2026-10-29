package fu.edu.sba301.assignment02.dto;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryDto {

    private Integer categoryId;

    @NotBlank(message = "Category name is required")
    private String categoryName;

    private String categoryDesciption;

    @NotNull(message = "Status is required (1: Active, 0: Inactive)")
    private Integer status = 1;

    public CategoryDto() {
    }

    public CategoryDto(Integer categoryId, String categoryName, String categoryDesciption, Integer status) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDesciption = categoryDesciption;
        this.status = status;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesciption() {
        return categoryDesciption;
    }

    public void setCategoryDesciption(String categoryDesciption) {
        this.categoryDesciption = categoryDesciption;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

