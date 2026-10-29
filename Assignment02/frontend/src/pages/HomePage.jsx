/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import React, { useEffect, useState } from 'react';
import { newsApi } from '../api/newsApi';
import { categoryApi } from '../api/categoryApi';
import { Modal } from '../components/Modal';

export const HomePage = () => {
  const [newsList, setNewsList] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState('');
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [selectedArticle, setSelectedArticle] = useState(null);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [newsData, catData] = await Promise.all([
        newsApi.getPublic(keyword),
        categoryApi.getActive()
      ]);
      setNewsList(newsData || []);
      setCategories(catData || []);
    } catch (err) {
      console.error('Failed to fetch public news:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [keyword]);

  const filteredNews = newsList
    .filter((item) => (selectedCategory ? item.categoryId === selectedCategory : true))
    .filter((item) => {
      if (!keyword || !keyword.trim()) return true;
      const kw = keyword.trim().toLowerCase();
      const titleMatch = item.newsTitle?.toLowerCase().includes(kw);
      const headlineMatch = item.headline?.toLowerCase().includes(kw);
      return titleMatch || headlineMatch;
    });

  return (
    <div className="container py-4">
      {/* Hero Header */}
      <div className="p-4 p-md-5 mb-4 rounded-3 text-white" style={{ background: 'linear-gradient(135deg, #0b2265 0%, #1e3c72 100%)' }}>
        <div className="col-md-8 px-0">
          <span className="badge bg-warning text-dark px-3 py-2 fw-bold mb-2">FUNewsManagementSystem</span>
          <h1 className="display-5 fw-bold">Trang Tin Tức Đại Học FPT</h1>
          <p className="lead my-3">
            Cập nhật những tin tức, hoạt động nghiên cứu, học tập và đời sống sinh viên mới nhất và chính xác nhất.
          </p>
        </div>
      </div>

      {/* Search and Category Filter */}
      <div className="row g-3 mb-4 align-items-center">
        <div className="col-md-6">
          <div className="input-group">
            <span className="input-group-text bg-white"><i className="bi bi-search"></i></span>
            <input
              type="text"
              className="form-control"
              placeholder="Tìm kiếm tin tức theo tiêu đề hoặc tóm tắt..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
            />
            {keyword && (
              <button className="btn btn-outline-secondary" onClick={() => setKeyword('')}>
                Xóa
              </button>
            )}
          </div>
        </div>

        <div className="col-md-6">
          <div className="d-flex flex-wrap gap-2 justify-content-md-end">
            <button
              className={`btn btn-sm ${selectedCategory === null ? 'btn-primary' : 'btn-outline-primary'}`}
              onClick={() => setSelectedCategory(null)}
            >
              Tất cả danh mục
            </button>
            {categories.map((cat) => (
              <button
                key={cat.categoryId}
                className={`btn btn-sm ${selectedCategory === cat.categoryId ? 'btn-primary' : 'btn-outline-primary'}`}
                onClick={() => setSelectedCategory(cat.categoryId)}
              >
                {cat.categoryName}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* News Grid */}
      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status"></div>
          <p className="mt-2 text-muted">Đang tải tin tức...</p>
        </div>
      ) : filteredNews.length === 0 ? (
        <div className="text-center py-5 bg-white rounded shadow-sm">
          <i className="bi bi-newspaper text-muted" style={{ fontSize: '3rem' }}></i>
          <h5 className="mt-3 text-muted">Chưa có bài viết nào phù hợp</h5>
        </div>
      ) : (
        <div className="row g-4">
          {filteredNews.map((article) => (
            <div key={article.newsArticleId} className="col-md-6 col-lg-4">
              <div className="card h-100 shadow-sm border-0 card-hover">
                <div className="card-body d-flex flex-column">
                  <div className="d-flex justify-content-between align-items-center mb-2">
                    <span className="badge bg-primary-subtle text-primary border border-primary-subtle">
                      {article.categoryName}
                    </span>
                    <small className="text-muted">
                      {article.createdDate ? new Date(article.createdDate).toLocaleDateString('vi-VN') : ''}
                    </small>
                  </div>
                  <h5 className="card-title fw-bold text-dark">{article.newsTitle}</h5>
                  <p className="card-text text-muted flex-grow-1">
                    {article.headline || (article.newsContent ? article.newsContent.substring(0, 100) + '...' : '')}
                  </p>

                  {article.tags && article.tags.length > 0 && (
                    <div className="d-flex flex-wrap gap-1 mb-3">
                      {article.tags.map((t) => (
                        <span key={t.tagId} className="badge bg-light text-secondary border">
                          #{t.tagName}
                        </span>
                      ))}
                    </div>
                  )}

                  <div className="d-flex justify-content-between align-items-center pt-2 border-top mt-auto">
                    <small className="text-muted">
                      <i className="bi bi-person me-1"></i>{article.createdByName}
                    </small>
                    <button
                      className="btn btn-outline-primary btn-sm"
                      onClick={() => setSelectedArticle(article)}
                    >
                      Đọc chi tiết <i className="bi bi-arrow-right"></i>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Article Detail Modal */}
      <Modal
        isOpen={!!selectedArticle}
        title={selectedArticle?.newsTitle || 'Chi tiết bài viết'}
        onClose={() => setSelectedArticle(null)}
      >
        {selectedArticle && (
          <div>
            <div className="d-flex flex-wrap gap-2 align-items-center mb-3">
              <span className="badge bg-primary">{selectedArticle.categoryName}</span>
              <span className="text-muted small">
                Nguồn: <strong>{selectedArticle.newsSource}</strong>
              </span>
              <span className="text-muted small">
                Tác giả: <strong>{selectedArticle.createdByName}</strong>
              </span>
              <span className="text-muted small">
                Ngày đăng: {selectedArticle.createdDate ? new Date(selectedArticle.createdDate).toLocaleString('vi-VN') : ''}
              </span>
            </div>

            {selectedArticle.headline && (
              <p className="fw-semibold text-secondary fst-italic border-start border-4 border-primary ps-3 my-3">
                {selectedArticle.headline}
              </p>
            )}

            <div className="news-body text-dark lh-lg" style={{ whiteSpace: 'pre-line' }}>
              {selectedArticle.newsContent}
            </div>

            {selectedArticle.tags && selectedArticle.tags.length > 0 && (
              <div className="mt-4 pt-3 border-top">
                <span className="me-2 small fw-bold text-muted">Từ khóa:</span>
                {selectedArticle.tags.map((t) => (
                  <span key={t.tagId} className="badge bg-secondary-subtle text-secondary me-1">
                    #{t.tagName}
                  </span>
                ))}
              </div>
            )}
          </div>
        )}
      </Modal>
    </div>
  );
};

