package fu.edu.sba301.assignment02.controller;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.NewsArticleRequest;
import fu.edu.sba301.assignment02.dto.NewsArticleResponse;
import fu.edu.sba301.assignment02.service.INewsArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsArticleController {

    private final INewsArticleService newsService;

    public NewsArticleController(INewsArticleService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<List<NewsArticleResponse>> getNews(
        @RequestParam(defaultValue = "false") boolean all,
        @RequestParam(required = false) String keyword
    ) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return ResponseEntity.ok(newsService.searchNews(keyword, !all));
        }
        if (all) {
            return ResponseEntity.ok(newsService.getAllNews());
        }
        return ResponseEntity.ok(newsService.getActiveNews());
    }

    @GetMapping("/public")
    public ResponseEntity<List<NewsArticleResponse>> getPublicActiveNews() {
        return ResponseEntity.ok(newsService.getActiveNews());
    }

    @GetMapping("/my-history")
    public ResponseEntity<List<NewsArticleResponse>> getMyNewsHistory(@RequestParam Integer staffId) {
        return ResponseEntity.ok(newsService.getNewsByStaff(staffId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsArticleResponse> getNewsById(@PathVariable Integer id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @PostMapping
    public ResponseEntity<NewsArticleResponse> createNews(
        @Valid @RequestBody NewsArticleRequest request,
        @RequestParam(required = false) Integer currentUserId
    ) {
        NewsArticleResponse created = newsService.createNews(request, currentUserId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsArticleResponse> updateNews(
        @PathVariable Integer id,
        @Valid @RequestBody NewsArticleRequest request
    ) {
        NewsArticleResponse updated = newsService.updateNews(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Integer id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
}
