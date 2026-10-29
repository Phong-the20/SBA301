package fu.edu.sba301.assignment02.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.NewsArticleRequest;
import fu.edu.sba301.assignment02.dto.NewsArticleResponse;
import fu.edu.sba301.assignment02.dto.TagDto;
import fu.edu.sba301.assignment02.entity.Category;
import fu.edu.sba301.assignment02.entity.NewsArticle;
import fu.edu.sba301.assignment02.entity.SystemAccount;
import fu.edu.sba301.assignment02.entity.Tag;
import fu.edu.sba301.assignment02.exception.BusinessException;
import fu.edu.sba301.assignment02.exception.ResourceNotFoundException;
import fu.edu.sba301.assignment02.repository.CategoryRepository;
import fu.edu.sba301.assignment02.repository.NewsArticleRepository;
import fu.edu.sba301.assignment02.repository.SystemAccountRepository;
import fu.edu.sba301.assignment02.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NewsArticleService implements INewsArticleService {

    private final NewsArticleRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final SystemAccountRepository accountRepository;
    private final TagRepository tagRepository;

    public NewsArticleService(
        NewsArticleRepository newsRepository,
        CategoryRepository categoryRepository,
        SystemAccountRepository accountRepository,
        TagRepository tagRepository
    ) {
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsArticleResponse> getAllNews() {
        return newsRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsArticleResponse> getActiveNews() {
        // Public requirement: "news status must be active (1)"
        return newsRepository.findByNewsStatus(1).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsArticleResponse getNewsById(Integer id) {
        NewsArticle article = newsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("News article not found with ID: " + id));
        return toResponse(article);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsArticleResponse> getNewsByStaff(Integer staffId) {
        // Staff requirement: "View news history created by him/her"
        return newsRepository.findByCreatedBy_AccountId(staffId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public NewsArticleResponse createNews(NewsArticleRequest request, Integer currentUserId) {
        if (request == null || request.getNewsTitle() == null || request.getNewsTitle().trim().isEmpty()) {
            throw new BusinessException("News title cannot be empty.");
        }
        if (request.getCategoryId() == null) {
            throw new BusinessException("Category ID must be provided.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));

        if (category.getStatus() == null || category.getStatus() == 0) {
            throw new BusinessException("Cannot assign news to an inactive category.");
        }

        final Integer finalAuthorId = (request.getCreatedBy() != null) ? request.getCreatedBy() :
            (currentUserId != null ? currentUserId : 2);

        SystemAccount author = accountRepository.findById(finalAuthorId)
            .orElseThrow(() -> new ResourceNotFoundException("Author account not found with ID: " + finalAuthorId));

        NewsArticle article = new NewsArticle();
        article.setNewsTitle(request.getNewsTitle().trim());
        article.setHeadline(request.getHeadline() != null ? request.getHeadline().trim() : "");
        article.setNewsContent(request.getNewsContent() != null ? request.getNewsContent().trim() : "");
        article.setNewsSource(request.getNewsSource() != null ? request.getNewsSource().trim() : "Internal");
        article.setCategory(category);
        article.setNewsStatus(request.getNewsStatus() != null ? request.getNewsStatus() : 1);
        article.setCreatedBy(author);
        article.setCreatedDate(LocalDateTime.now());
        article.setModifiedDate(LocalDateTime.now());

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            article.setTags(tags);
        }

        NewsArticle saved = newsRepository.save(article);
        return toResponse(saved);
    }

    @Override
    public NewsArticleResponse updateNews(Integer id, NewsArticleRequest request) {
        NewsArticle existing = newsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("News article not found with ID: " + id));

        if (request.getCategoryId() != null && !request.getCategoryId().equals(existing.getCategory().getCategoryId())) {
            Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));
            if (category.getStatus() == null || category.getStatus() == 0) {
                throw new BusinessException("Cannot assign news to an inactive category.");
            }
            existing.setCategory(category);
        }

        if (request.getNewsTitle() != null && !request.getNewsTitle().trim().isEmpty()) {
            existing.setNewsTitle(request.getNewsTitle().trim());
        }
        if (request.getHeadline() != null) {
            existing.setHeadline(request.getHeadline().trim());
        }
        if (request.getNewsContent() != null) {
            existing.setNewsContent(request.getNewsContent().trim());
        }
        if (request.getNewsSource() != null) {
            existing.setNewsSource(request.getNewsSource().trim());
        }
        if (request.getNewsStatus() != null) {
            existing.setNewsStatus(request.getNewsStatus());
        }
        if (request.getTagIds() != null) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            existing.setTags(tags);
        }
        existing.setModifiedDate(LocalDateTime.now());

        NewsArticle saved = newsRepository.save(existing);
        return toResponse(saved);
    }

    @Override
    public void deleteNews(Integer id) {
        NewsArticle existing = newsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("News article not found with ID: " + id));
        newsRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsArticleResponse> searchNews(String keyword, boolean activeOnly) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return activeOnly ? getActiveNews() : getAllNews();
        }
        String clean = keyword.trim();
        List<NewsArticle> list;
        if (activeOnly) {
            list = newsRepository.searchByStatusAndKeyword(1, clean);
        } else {
            list = newsRepository.searchAllByKeyword(clean);
        }
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private NewsArticleResponse toResponse(NewsArticle entity) {
        NewsArticleResponse res = new NewsArticleResponse();
        res.setNewsArticleId(entity.getNewsArticleId());
        res.setNewsTitle(entity.getNewsTitle());
        res.setHeadline(entity.getHeadline());
        res.setCreatedDate(entity.getCreatedDate());
        res.setNewsContent(entity.getNewsContent());
        res.setNewsSource(entity.getNewsSource());
        if (entity.getCategory() != null) {
            res.setCategoryId(entity.getCategory().getCategoryId());
            res.setCategoryName(entity.getCategory().getCategoryName());
        }
        res.setNewsStatus(entity.getNewsStatus());
        if (entity.getCreatedBy() != null) {
            res.setCreatedById(entity.getCreatedBy().getAccountId());
            res.setCreatedByName(entity.getCreatedBy().getAccountName());
        }
        res.setModifiedDate(entity.getModifiedDate());
        if (entity.getTags() != null) {
            res.setTags(entity.getTags().stream()
                .map(t -> new TagDto(t.getTagId(), t.getTagName(), t.getNote()))
                .collect(Collectors.toList()));
        }
        return res;
    }
}
