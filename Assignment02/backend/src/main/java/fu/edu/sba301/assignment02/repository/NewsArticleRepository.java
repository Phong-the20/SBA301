package fu.edu.sba301.assignment02.repository;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Integer> {
    boolean existsByCreatedBy_AccountId(Integer accountId);
    boolean existsByCategory_CategoryId(Integer categoryId);

    List<NewsArticle> findByNewsStatus(Integer newsStatus);
    List<NewsArticle> findByCreatedBy_AccountId(Integer accountId);
    List<NewsArticle> findByNewsTitleContainingIgnoreCaseOrHeadlineContainingIgnoreCase(String title, String headline);
    List<NewsArticle> findByNewsStatusAndNewsTitleContainingIgnoreCase(Integer status, String title);
}
