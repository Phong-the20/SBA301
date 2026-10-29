package fu.edu.sba301.assignment02.dto;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class NewsArticleRequest {

    private Integer newsArticleId;

    @NotBlank(message = "News title is required")
    private String newsTitle;

    private String headline;

    private String newsContent;

    private String newsSource;

    @NotNull(message = "Category is required")
    private Integer categoryId;

    @NotNull(message = "News status is required (1: Active, 0: Inactive)")
    private Integer newsStatus = 1;

    private Integer createdBy; // Staff account ID

    private List<Integer> tagIds = new ArrayList<>();

    public NewsArticleRequest() {
    }

    public Integer getNewsArticleId() {
        return newsArticleId;
    }

    public void setNewsArticleId(Integer newsArticleId) {
        this.newsArticleId = newsArticleId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getNewsStatus() {
        return newsStatus;
    }

    public void setNewsStatus(Integer newsStatus) {
        this.newsStatus = newsStatus;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }
}

