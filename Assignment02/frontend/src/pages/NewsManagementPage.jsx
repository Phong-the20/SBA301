/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import React, { useEffect, useState } from 'react';
import { newsApi } from '../api/newsApi';
import { categoryApi } from '../api/categoryApi';
import { tagApi } from '../api/tagApi';
import { useAuth } from '../context/AuthContext';
import { Modal } from '../components/Modal';
import { ConfirmModal } from '../components/ConfirmModal';

export const NewsManagementPage = () => {
  const { user } = useAuth();
  const [newsList, setNewsList] = useState([]);
  const [categories, setCategories] = useState([]);
  const [allTags, setAllTags] = useState([]);
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [errorAlert, setErrorAlert] = useState('');
  const [successAlert, setSuccessAlert] = useState('');

  // Modal State for Create / Edit
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    newsArticleId: null,
    newsTitle: '',
    headline: '',
    newsContent: '',
    newsSource: '',
    categoryId: '',
    newsStatus: 1,
    tagIds: []
  });

  // Modal State for Delete
  const [deleteId, setDeleteId] = useState(null);
  const [newsToDelete, setNewsToDelete] = useState(null);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [newsData, catData, tagData] = await Promise.all([
        newsApi.getAll(keyword),
        categoryApi.getAll(),
        tagApi.getAll()
      ]);
      setNewsList(newsData || []);
      setCategories(catData || []);
      setAllTags(tagData || []);
    } catch (err) {
      setErrorAlert(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [keyword]);

  const handleOpenCreate = () => {
    setIsEditing(false);
    const activeCats = categories.filter(c => c.status === 1);
    setFormData({
      newsArticleId: null,
      newsTitle: '',
      headline: '',
      newsContent: '',
      newsSource: 'FPT University',
      categoryId: activeCats.length > 0 ? activeCats[0].categoryId : '',
      newsStatus: 1,
      tagIds: []
    });
    setErrorAlert('');
    setIsModalOpen(true);
  };

  const handleOpenEdit = (article) => {
    setIsEditing(true);
    setFormData({
      newsArticleId: article.newsArticleId,
      newsTitle: article.newsTitle,
      headline: article.headline || '',
      newsContent: article.newsContent || '',
      newsSource: article.newsSource || '',
      categoryId: article.categoryId,
      newsStatus: article.newsStatus,
      tagIds: article.tags ? article.tags.map(t => t.tagId) : []
    });
    setErrorAlert('');
    setIsModalOpen(true);
  };

  const handleTagToggle = (tagId) => {
    setFormData(prev => {
      const exists = prev.tagIds.includes(tagId);
      return {
        ...prev,
        tagIds: exists ? prev.tagIds.filter(id => id !== tagId) : [...prev.tagIds, tagId]
      };
    });
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setErrorAlert('');
    setSuccessAlert('');
    try {
      if (isEditing) {
        await newsApi.update(formData.newsArticleId, formData);
        setSuccessAlert('Cập nhật bài viết thành công!');
      } else {
        await newsApi.create(formData, user?.accountId);
        setSuccessAlert('Tạo bài viết mới thành công!');
      }
      setIsModalOpen(false);
      fetchData();
    } catch (err) {
      setErrorAlert(err.message);
    }
  };

  const handleOpenDelete = (article) => {
    setDeleteId(article.newsArticleId);
    setNewsToDelete(article);
    setErrorAlert('');
  };

  const handleConfirmDelete = async () => {
    if (!deleteId) return;
    setErrorAlert('');
    setSuccessAlert('');
    try {
      await newsApi.delete(deleteId);
      setSuccessAlert('Xóa bài viết thành công!');
      setDeleteId(null);
      setNewsToDelete(null);
      fetchData();
    } catch (err) {
      setErrorAlert(err.message);
      setDeleteId(null);
      setNewsToDelete(null);
    }
  };

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
          <h2 className="fw-bold text-dark mb-1">
            <i className="bi bi-newspaper text-info me-2"></i>Quản Lý Bài Viết Tin Tức (Staff)
          </h2>
          <p className="text-muted small mb-0">Quản lý bài viết, chuyên mục, từ khóa và trạng thái xuất bản</p>
        </div>
        <button className="btn btn-info text-white shadow-sm" onClick={handleOpenCreate}>
          <i className="bi bi-file-earmark-plus-fill me-1"></i>Tạo bài viết mới
        </button>
      </div>

      {/* Alerts */}
      {errorAlert && (
        <div className="alert alert-danger alert-dismissible fade show d-flex align-items-center gap-2" role="alert">
          <i className="bi bi-exclamation-triangle-fill fs-5"></i>
          <div>{errorAlert}</div>
          <button type="button" className="btn-close" onClick={() => setErrorAlert('')}></button>
        </div>
      )}

      {successAlert && (
        <div className="alert alert-success alert-dismissible fade show d-flex align-items-center gap-2" role="alert">
          <i className="bi bi-check-circle-fill fs-5"></i>
          <div>{successAlert}</div>
          <button type="button" className="btn-close" onClick={() => setSuccessAlert('')}></button>
        </div>
      )}

      {/* Search Bar */}
      <div className="card shadow-sm border-0 mb-4 p-3 bg-white">
        <div className="input-group">
          <span className="input-group-text bg-light"><i className="bi bi-search"></i></span>
          <input
            type="text"
            className="form-control"
            placeholder="Tìm kiếm bài viết theo tiêu đề..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
        </div>
      </div>

      {/* Table */}
      <div className="card shadow-sm border-0">
        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0">
            <thead className="table-light">
              <tr>
                <th style={{ width: '70px' }}>ID</th>
                <th>Tiêu đề bài viết</th>
                <th>Danh mục</th>
                <th>Tác giả</th>
                <th>Từ khóa (Tags)</th>
                <th>Trạng thái</th>
                <th className="text-end" style={{ width: '160px' }}>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="7" className="text-center py-4 text-muted">
                    <div className="spinner-border spinner-border-sm me-2"></div> Đang tải bài viết...
                  </td>
                </tr>
              ) : newsList.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-4 text-muted">Không có bài viết nào.</td>
                </tr>
              ) : (
                newsList.map((article) => (
                  <tr key={article.newsArticleId}>
                    <td className="fw-bold">#{article.newsArticleId}</td>
                    <td>
                      <div className="fw-semibold text-dark">{article.newsTitle}</div>
                      <small className="text-muted">{article.headline}</small>
                    </td>
                    <td>
                      <span className="badge bg-light text-primary border">{article.categoryName}</span>
                    </td>
                    <td><small className="text-secondary">{article.createdByName}</small></td>
                    <td>
                      <div className="d-flex flex-wrap gap-1">
                        {article.tags?.map((t) => (
                          <span key={t.tagId} className="badge bg-secondary-subtle text-secondary small">
                            #{t.tagName}
                          </span>
                        ))}
                      </div>
                    </td>
                    <td>
                      <span className={`badge ${article.newsStatus === 1 ? 'bg-success' : 'bg-secondary'}`}>
                        {article.newsStatus === 1 ? 'Active (1)' : 'Inactive (0)'}
                      </span>
                    </td>
                    <td className="text-end">
                      <button
                        className="btn btn-outline-secondary btn-sm me-2"
                        title="Chỉnh sửa bài viết"
                        onClick={() => handleOpenEdit(article)}
                      >
                        <i className="bi bi-pencil-square"></i>
                      </button>
                      <button
                        className="btn btn-outline-danger btn-sm"
                        title="Xóa bài viết"
                        onClick={() => handleOpenDelete(article)}
                      >
                        <i className="bi bi-trash-fill"></i>
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Popup Modal for Create/Update News */}
      <Modal
        isOpen={isModalOpen}
        title={isEditing ? 'Chỉnh sửa bài viết tin tức' : 'Tạo mới bài viết tin tức'}
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleSave}>
          <div className="mb-3">
            <label className="form-label fw-semibold">Tiêu đề bài viết <span className="text-danger">*</span></label>
            <input
              type="text"
              className="form-control"
              required
              placeholder="Nhập tiêu đề tin tức..."
              value={formData.newsTitle}
              onChange={(e) => setFormData({ ...formData, newsTitle: e.target.value })}
            />
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Tóm tắt / Headline</label>
            <input
              type="text"
              className="form-control"
              placeholder="Tóm tắt ngắn gọn hiển thị ngoài trang chủ..."
              value={formData.headline}
              onChange={(e) => setFormData({ ...formData, headline: e.target.value })}
            />
          </div>

          <div className="row g-3 mb-3">
            <div className="col-md-6">
              <label className="form-label fw-semibold">Danh mục <span className="text-danger">*</span></label>
              <select
                className="form-select"
                required
                value={formData.categoryId}
                onChange={(e) => setFormData({ ...formData, categoryId: Number(e.target.value) })}
              >
                <option value="">-- Chọn danh mục --</option>
                {categories.map((c) => (
                  <option key={c.categoryId} value={c.categoryId} disabled={c.status === 0}>
                    {c.categoryName} {c.status === 0 ? '(Ngưng hoạt động)' : ''}
                  </option>
                ))}
              </select>
            </div>

            <div className="col-md-6">
              <label className="form-label fw-semibold">Trạng thái xuất bản <span className="text-danger">*</span></label>
              <select
                className="form-select"
                value={formData.newsStatus}
                onChange={(e) => setFormData({ ...formData, newsStatus: Number(e.target.value) })}
              >
                <option value={1}>Active (Công khai cho người xem - 1)</option>
                <option value={0}>Inactive (Ẩn / Bản nháp - 0)</option>
              </select>
            </div>
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Nguồn tin tức</label>
            <input
              type="text"
              className="form-control"
              placeholder="VD: Báo Dân Trí, Phòng Đào Tạo..."
              value={formData.newsSource}
              onChange={(e) => setFormData({ ...formData, newsSource: e.target.value })}
            />
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Nội dung chi tiết</label>
            <textarea
              className="form-control"
              rows="5"
              placeholder="Nhập toàn bộ nội dung bài viết..."
              value={formData.newsContent}
              onChange={(e) => setFormData({ ...formData, newsContent: e.target.value })}
            ></textarea>
          </div>

          {/* Tags Checkbox selection */}
          <div className="mb-4">
            <label className="form-label fw-semibold">Gắn từ khóa (Tags):</label>
            <div className="d-flex flex-wrap gap-2 p-2 border rounded bg-light">
              {allTags.map((t) => {
                const checked = formData.tagIds.includes(t.tagId);
                return (
                  <div key={t.tagId} className="form-check">
                    <input
                      className="form-check-input"
                      type="checkbox"
                      id={`tag-${t.tagId}`}
                      checked={checked}
                      onChange={() => handleTagToggle(t.tagId)}
                    />
                    <label className="form-check-label small" htmlFor={`tag-${t.tagId}`}>
                      #{t.tagName}
                    </label>
                  </div>
                );
              })}
            </div>
          </div>

          <div className="d-flex justify-content-end gap-2 border-top pt-3">
            <button type="button" className="btn btn-secondary" onClick={() => setIsModalOpen(false)}>
              Hủy
            </button>
            <button type="submit" className="btn btn-info text-white px-4">
              {isEditing ? 'Lưu thay đổi' : 'Đăng bài'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <ConfirmModal
        isOpen={!!deleteId}
        title="Xác nhận xóa bài viết"
        message={`Bạn có chắc muốn xóa bài viết "${newsToDelete?.newsTitle}" không? Hành động này sẽ xóa vĩnh viễn bài viết khỏi hệ thống.`}
        onConfirm={handleConfirmDelete}
        onCancel={() => { setDeleteId(null); setNewsToDelete(null); }}
      />
    </div>
  );
};
