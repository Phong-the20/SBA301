package fu.edu.sba301.assignment02;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.NewsArticleRequest;
import fu.edu.sba301.assignment02.dto.NewsArticleResponse;
import fu.edu.sba301.assignment02.entity.Category;
import fu.edu.sba301.assignment02.entity.NewsArticle;
import fu.edu.sba301.assignment02.entity.SystemAccount;
import fu.edu.sba301.assignment02.exception.BusinessException;
import fu.edu.sba301.assignment02.repository.CategoryRepository;
import fu.edu.sba301.assignment02.repository.NewsArticleRepository;
import fu.edu.sba301.assignment02.repository.SystemAccountRepository;
import fu.edu.sba301.assignment02.repository.TagRepository;
import fu.edu.sba301.assignment02.service.NewsArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsArticleServiceTests {

    @Mock
    private NewsArticleRepository newsRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SystemAccountRepository accountRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private NewsArticleService newsService;

    @Test
    @DisplayName("Public active news filter returns only news with status 1")
    void testGetActiveNewsReturnsOnlyActive() {
        Category cat = new Category(1, "Tech", "Tech news", 1);
        SystemAccount author = new SystemAccount(2, "Staff", "staff@news.com", 2, "pass");

        NewsArticle activeNews = new NewsArticle();
        activeNews.setNewsArticleId(101);
        activeNews.setNewsTitle("Active Title");
        activeNews.setNewsStatus(1);
        activeNews.setCategory(cat);
        activeNews.setCreatedBy(author);

        when(newsRepository.findByNewsStatus(1)).thenReturn(List.of(activeNews));

        List<NewsArticleResponse> results = newsService.getActiveNews();
        assertEquals(1, results.size());
        assertEquals(101, results.get(0).getNewsArticleId());
        assertEquals(1, results.get(0).getNewsStatus());
    }

    @Test
    @DisplayName("Create news throws exception if assigned category is inactive")
    void testCreateNewsThrowsOnInactiveCategory() {
        Category inactiveCategory = new Category(2, "Archived", "Desc", 0); // status = 0

        NewsArticleRequest req = new NewsArticleRequest();
        req.setNewsTitle("Test Title");
        req.setCategoryId(2);

        when(categoryRepository.findById(2)).thenReturn(Optional.of(inactiveCategory));

        BusinessException ex = assertThrows(BusinessException.class, () -> newsService.createNews(req, 2));
        assertTrue(ex.getMessage().contains("inactive category"));
        verify(newsRepository, never()).save(any());
    }
}
