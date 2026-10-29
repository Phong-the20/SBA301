package fu.edu.sba301.slot32.service;

import fu.edu.sba301.slot32.model.Category;
import fu.edu.sba301.slot32.repository.ICategoryRepository;
import fu.edu.sba301.slot32.repository.IProductRepository;

import java.util.List;

public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final IProductRepository productRepository;

    public CategoryService(ICategoryRepository categoryRepository, IProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Category createCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException("Category name is required and cannot be blank");
        }
        if (category.getId() != null && categoryRepository.existsById(category.getId())) {
            throw new IllegalStateException("Category ID already exists: " + category.getId());
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category getById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Category category) {
        if (category == null || category.getId() == null) {
            throw new IllegalArgumentException("Category and Category ID are required for update");
        }
        if (!categoryRepository.existsById(category.getId())) {
            throw new IllegalArgumentException("Cannot update non-existent category: " + category.getId());
        }
        if (category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException("Category name cannot be blank");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found with ID: " + id);
        }
        // Business Rule / Referential Integrity check in Service Layer:
        // Prevent deleting category if any product references it!
        long referencingProducts = productRepository.findByCategoryId(id).size();
        if (referencingProducts > 0) {
            throw new IllegalStateException("Cannot delete category '" + id +
                    "': " + referencingProducts + " products are referencing this category!");
        }
        categoryRepository.deleteById(id);
    }
}
