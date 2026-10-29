package fu.edu.sba301.assignment02;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.CategoryDto;
import fu.edu.sba301.assignment02.entity.Category;
import fu.edu.sba301.assignment02.exception.BusinessException;
import fu.edu.sba301.assignment02.repository.CategoryRepository;
import fu.edu.sba301.assignment02.repository.NewsArticleRepository;
import fu.edu.sba301.assignment02.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private NewsArticleRepository newsArticleRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category(5, "Du Lịch", "Tin tức khám phá", 1);
    }

    @Test
    @DisplayName("Create category throws exception if name already exists")
    void testCreateCategoryDuplicateName() {
        CategoryDto dto = new CategoryDto(null, "Du Lịch", "Desc", 1);
        when(categoryRepository.existsByCategoryNameIgnoreCase("Du Lịch")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> categoryService.createCategory(dto));
        assertTrue(ex.getMessage().contains("already exists"));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Delete category throws BusinessException when category is linked to news articles")
    void testDeleteCategoryBlockedByNewsArticles() {
        when(categoryRepository.findById(5)).thenReturn(Optional.of(sampleCategory));
        // Critical requirement: category stored in news article -> cannot delete
        when(newsArticleRepository.existsByCategory_CategoryId(5)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> categoryService.deleteCategory(5));
        assertTrue(ex.getMessage().contains("linked to existing news articles"));
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Delete category succeeds when unused")
    void testDeleteCategorySuccessWhenUnused() {
        when(categoryRepository.findById(5)).thenReturn(Optional.of(sampleCategory));
        when(newsArticleRepository.existsByCategory_CategoryId(5)).thenReturn(false);

        assertDoesNotThrow(() -> categoryService.deleteCategory(5));
        verify(categoryRepository, times(1)).delete(sampleCategory);
    }
}
