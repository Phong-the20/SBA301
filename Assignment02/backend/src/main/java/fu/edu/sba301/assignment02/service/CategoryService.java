package fu.edu.sba301.assignment02.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.CategoryDto;
import fu.edu.sba301.assignment02.entity.Category;
import fu.edu.sba301.assignment02.exception.BusinessException;
import fu.edu.sba301.assignment02.exception.ResourceNotFoundException;
import fu.edu.sba301.assignment02.repository.CategoryRepository;
import fu.edu.sba301.assignment02.repository.NewsArticleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final NewsArticleRepository newsArticleRepository;

    public CategoryService(CategoryRepository categoryRepository, NewsArticleRepository newsArticleRepository) {
        this.categoryRepository = categoryRepository;
        this.newsArticleRepository = newsArticleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getActiveCategories() {
        return categoryRepository.findByStatus(1).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        return toDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        if (dto == null || dto.getCategoryName() == null || dto.getCategoryName().trim().isEmpty()) {
            throw new BusinessException("Category name cannot be empty.");
        }
        String name = dto.getCategoryName().trim();
        if (categoryRepository.existsByCategoryNameIgnoreCase(name)) {
            throw new BusinessException("Category name '" + name + "' already exists.");
        }

        Category category = new Category();
        category.setCategoryName(name);
        category.setCategoryDesciption(dto.getCategoryDesciption() != null ? dto.getCategoryDesciption().trim() : "");
        category.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);

        Category saved = categoryRepository.save(category);
        return toDto(saved);
    }

    @Override
    public CategoryDto updateCategory(Integer id, CategoryDto dto) {
        Category existing = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        String newName = dto.getCategoryName().trim();
        if (!existing.getCategoryName().equalsIgnoreCase(newName) && categoryRepository.existsByCategoryNameIgnoreCase(newName)) {
            throw new BusinessException("Category name '" + newName + "' already exists.");
        }

        existing.setCategoryName(newName);
        existing.setCategoryDesciption(dto.getCategoryDesciption() != null ? dto.getCategoryDesciption().trim() : "");
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }

        Category saved = categoryRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category existing = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // Critical Business Requirement:
        // "The delete action will delete an item in the case this item is not belong to any news articles.
        // If the item is already stored in a news article cannot delete."
        boolean isUsed = newsArticleRepository.existsByCategory_CategoryId(id);
        if (isUsed) {
            throw new BusinessException("Cannot delete category '" + existing.getCategoryName() +
                "' (ID: " + id + ") because it is currently linked to existing news articles.");
        }

        categoryRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> searchCategories(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCategories();
        }
        return categoryRepository.findByCategoryNameContainingIgnoreCase(keyword.trim()).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private CategoryDto toDto(Category entity) {
        return new CategoryDto(
            entity.getCategoryId(),
            entity.getCategoryName(),
            entity.getCategoryDesciption(),
            entity.getStatus()
        );
    }
}

