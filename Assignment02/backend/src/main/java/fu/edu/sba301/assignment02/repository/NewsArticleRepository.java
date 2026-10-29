package fu.edu.sba301.assignment02.repository;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT * FROM news_article WHERE news_status = :status AND (news_title COLLATE SQL_Latin1_General_CP1_CI_AI LIKE CONCAT('%', :kw, '%') OR headline COLLATE SQL_Latin1_General_CP1_CI_AI LIKE CONCAT('%', :kw, '%'))", nativeQuery = true)
    List<NewsArticle> searchByStatusAndKeyword(@Param("status") Integer status, @Param("kw") String keyword);

    @Query(value = "SELECT * FROM news_article WHERE news_title COLLATE SQL_Latin1_General_CP1_CI_AI LIKE CONCAT('%', :kw, '%') OR headline COLLATE SQL_Latin1_General_CP1_CI_AI LIKE CONCAT('%', :kw, '%')", nativeQuery = true)
    List<NewsArticle> searchAllByKeyword(@Param("kw") String keyword);
}

