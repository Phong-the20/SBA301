package fu.edu.sba301.assignment02.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.NewsArticleRequest;
import fu.edu.sba301.assignment02.dto.NewsArticleResponse;
import java.util.List;

public interface INewsArticleService {
    List<NewsArticleResponse> getAllNews();
    List<NewsArticleResponse> getActiveNews();
    NewsArticleResponse getNewsById(Integer id);
    List<NewsArticleResponse> getNewsByStaff(Integer staffId);
    NewsArticleResponse createNews(NewsArticleRequest request, Integer currentUserId);
    NewsArticleResponse updateNews(Integer id, NewsArticleRequest request);
    void deleteNews(Integer id);
    List<NewsArticleResponse> searchNews(String keyword, boolean activeOnly);
}
