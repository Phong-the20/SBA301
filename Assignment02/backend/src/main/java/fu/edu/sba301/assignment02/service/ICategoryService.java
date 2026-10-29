package fu.edu.sba301.assignment02.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.CategoryDto;
import java.util.List;

public interface ICategoryService {
    List<CategoryDto> getAllCategories();
    List<CategoryDto> getActiveCategories();
    CategoryDto getCategoryById(Integer id);
    CategoryDto createCategory(CategoryDto dto);
    CategoryDto updateCategory(Integer id, CategoryDto dto);
    void deleteCategory(Integer id);
    List<CategoryDto> searchCategories(String keyword);
}

