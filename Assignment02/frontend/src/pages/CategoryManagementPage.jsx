/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import React, { useEffect, useState } from 'react';
import { categoryApi } from '../api/categoryApi';
import { Modal } from '../components/Modal';
import { ConfirmModal } from '../components/ConfirmModal';

export const CategoryManagementPage = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [errorAlert, setErrorAlert] = useState('');
  const [successAlert, setSuccessAlert] = useState('');

  // Modal State for Create / Edit
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    categoryId: null,
    categoryName: '',
    categoryDesciption: '',
    status: 1
  });

  // Modal State for Delete Confirmation
  const [deleteId, setDeleteId] = useState(null);
  const [categoryToDelete, setCategoryToDelete] = useState(null);

  const fetchCategories = async () => {
    try {
      setLoading(true);
      const data = await categoryApi.getAll(keyword);
      setCategories(data || []);
    } catch (err) {
      setErrorAlert(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, [keyword]);

  const handleOpenCreate = () => {
    setIsEditing(false);
    setFormData({
      categoryId: null,
      categoryName: '',
      categoryDesciption: '',
      status: 1
    });
    setErrorAlert('');
    setIsModalOpen(true);
  };

  const handleOpenEdit = (cat) => {
    setIsEditing(true);
    setFormData({
      categoryId: cat.categoryId,
      categoryName: cat.categoryName,
      categoryDesciption: cat.categoryDesciption || '',
      status: cat.status
    });
    setErrorAlert('');
    setIsModalOpen(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setErrorAlert('');
    setSuccessAlert('');
    try {
      if (isEditing) {
        await categoryApi.update(formData.categoryId, formData);
        setSuccessAlert('Cập nhật danh mục thành công!');
      } else {
        await categoryApi.create(formData);
        setSuccessAlert('Tạo danh mục mới thành công!');
      }
      setIsModalOpen(false);
      fetchCategories();
    } catch (err) {
      setErrorAlert(err.message);
    }
  };

  const handleOpenDelete = (cat) => {
    setDeleteId(cat.categoryId);
    setCategoryToDelete(cat);
    setErrorAlert('');
  };

  const handleConfirmDelete = async () => {
    if (!deleteId) return;
    setErrorAlert('');
    setSuccessAlert('');
    try {
      await categoryApi.delete(deleteId);
      setSuccessAlert('Xóa danh mục thành công!');
      setDeleteId(null);
      setCategoryToDelete(null);
      fetchCategories();
    } catch (err) {
      // Catches the critical business exception:
      // "The delete action will delete an item in the case this item is not belong to any news articles.
      // If the item is already stored in a news article cannot delete."
      setErrorAlert(err.message);
      setDeleteId(null);
      setCategoryToDelete(null);
    }
  };

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
          <h2 className="fw-bold text-dark mb-1">
            <i className="bi bi-tags-fill text-info me-2"></i>Quản Lý Danh Mục Tin Tức (Staff)
          </h2>
          <p className="text-muted small mb-0">Quản lý và phân loại các chuyên mục tin tức trong hệ thống</p>
        </div>
        <button className="btn btn-info text-white shadow-sm" onClick={handleOpenCreate}>
          <i className="bi bi-plus-circle-fill me-1"></i>Thêm danh mục mới
        </button>
      </div>

      {/* Notifications */}
      {errorAlert && (
        <div className="alert alert-danger alert-dismissible fade show d-flex align-items-center gap-2" role="alert">
          <i className="bi bi-shield-x fs-4"></i>
          <div><strong>Ràng buộc hệ thống:</strong> {errorAlert}</div>
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

      {/* Search Input */}
      <div className="card shadow-sm border-0 mb-4 p-3 bg-white">
        <div className="input-group">
          <span className="input-group-text bg-light"><i className="bi bi-search"></i></span>
          <input
            type="text"
            className="form-control"
            placeholder="Tìm kiếm danh mục theo tên..."
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
                <th style={{ width: '80px' }}>ID</th>
                <th>Tên danh mục</th>
                <th>Mô tả chuyên mục</th>
                <th>Trạng thái</th>
                <th className="text-end" style={{ width: '180px' }}>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="5" className="text-center py-4 text-muted">
                    <div className="spinner-border spinner-border-sm me-2"></div> Đang tải danh sách...
                  </td>
                </tr>
              ) : categories.length === 0 ? (
                <tr>
                  <td colSpan="5" className="text-center py-4 text-muted">Không tìm thấy danh mục nào.</td>
                </tr>
              ) : (
                categories.map((cat) => (
                  <tr key={cat.categoryId}>
                    <td className="fw-bold">#{cat.categoryId}</td>
                    <td className="fw-semibold text-dark">{cat.categoryName}</td>
                    <td className="text-muted">{cat.categoryDesciption || '—'}</td>
                    <td>
                      <span className={`badge ${cat.status === 1 ? 'bg-success' : 'bg-secondary'}`}>
                        {cat.status === 1 ? 'Active (1)' : 'Inactive (0)'}
                      </span>
                    </td>
                    <td className="text-end">
                      <button
                        className="btn btn-outline-secondary btn-sm me-2"
                        title="Chỉnh sửa"
                        onClick={() => handleOpenEdit(cat)}
                      >
                        <i className="bi bi-pencil-square"></i>
                      </button>
                      <button
                        className="btn btn-outline-danger btn-sm"
                        title="Xóa danh mục"
                        onClick={() => handleOpenDelete(cat)}
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

      {/* Popup Modal for Create/Update */}
      <Modal
        isOpen={isModalOpen}
        title={isEditing ? 'Chỉnh sửa danh mục' : 'Thêm danh mục mới'}
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleSave}>
          <div className="mb-3">
            <label className="form-label fw-semibold">Tên danh mục <span className="text-danger">*</span></label>
            <input
              type="text"
              className="form-control"
              required
              placeholder="VD: Công nghệ thông tin"
              value={formData.categoryName}
              onChange={(e) => setFormData({ ...formData, categoryName: e.target.value })}
            />
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Mô tả chuyên mục</label>
            <textarea
              className="form-control"
              rows="3"
              placeholder="Mô tả tóm tắt về loại tin tức thuộc danh mục này"
              value={formData.categoryDesciption}
              onChange={(e) => setFormData({ ...formData, categoryDesciption: e.target.value })}
            ></textarea>
          </div>

          <div className="mb-4">
            <label className="form-label fw-semibold">Trạng thái <span className="text-danger">*</span></label>
            <select
              className="form-select"
              value={formData.status}
              onChange={(e) => setFormData({ ...formData, status: Number(e.target.value) })}
            >
              <option value={1}>Active (Hoạt động - 1)</option>
              <option value={0}>Inactive (Ngưng hoạt động - 0)</option>
            </select>
          </div>

          <div className="d-flex justify-content-end gap-2 border-top pt-3">
            <button type="button" className="btn btn-secondary" onClick={() => setIsModalOpen(false)}>
              Hủy
            </button>
            <button type="submit" className="btn btn-info text-white px-4">
              {isEditing ? 'Lưu thay đổi' : 'Tạo mới'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <ConfirmModal
        isOpen={!!deleteId}
        title="Xác nhận xóa danh mục"
        message={`Bạn có chắc chắn muốn xóa danh mục "${categoryToDelete?.categoryName}" không? Nếu danh mục này đang có bài viết, hệ thống sẽ ngăn chặn việc xóa.`}
        onConfirm={handleConfirmDelete}
        onCancel={() => { setDeleteId(null); setCategoryToDelete(null); }}
      />
    </div>
  );
};
