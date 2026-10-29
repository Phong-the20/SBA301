/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import React, { useEffect, useState } from 'react';
import { accountApi } from '../api/accountApi';
import { Modal } from '../components/Modal';
import { ConfirmModal } from '../components/ConfirmModal';

export const AccountManagementPage = () => {
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [errorAlert, setErrorAlert] = useState('');
  const [successAlert, setSuccessAlert] = useState('');

  // Modal State for Create / Edit
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    accountId: null,
    accountName: '',
    accountEmail: '',
    accountRole: 2,
    accountPassword: ''
  });

  // Modal State for Delete Confirmation
  const [deleteId, setDeleteId] = useState(null);
  const [accountToDelete, setAccountToDelete] = useState(null);

  const fetchAccounts = async () => {
    try {
      setLoading(true);
      const data = await accountApi.getAll(keyword);
      setAccounts(data || []);
    } catch (err) {
      setErrorAlert(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAccounts();
  }, [keyword]);

  const handleOpenCreate = () => {
    setIsEditing(false);
    setFormData({
      accountId: null,
      accountName: '',
      accountEmail: '',
      accountRole: 2,
      accountPassword: ''
    });
    setErrorAlert('');
    setIsModalOpen(true);
  };

  const handleOpenEdit = (acc) => {
    setIsEditing(true);
    setFormData({
      accountId: acc.accountId,
      accountName: acc.accountName,
      accountEmail: acc.accountEmail,
      accountRole: acc.accountRole,
      accountPassword: ''
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
        await accountApi.update(formData.accountId, formData);
        setSuccessAlert('Cập nhật tài khoản thành công!');
      } else {
        await accountApi.create(formData);
        setSuccessAlert('Tạo tài khoản mới thành công!');
      }
      setIsModalOpen(false);
      fetchAccounts();
    } catch (err) {
      setErrorAlert(err.message);
    }
  };

  const handleOpenDelete = (acc) => {
    setDeleteId(acc.accountId);
    setAccountToDelete(acc);
    setErrorAlert('');
  };

  const handleConfirmDelete = async () => {
    if (!deleteId) return;
    setErrorAlert('');
    setSuccessAlert('');
    try {
      await accountApi.delete(deleteId);
      setSuccessAlert('Xóa tài khoản thành công!');
      setDeleteId(null);
      setAccountToDelete(null);
      fetchAccounts();
    } catch (err) {
      // Catches the critical business exception:
      // "the delete action will delete an account in the case this account is not belong to any news articles (created)..."
      setErrorAlert(err.message);
      setDeleteId(null);
      setAccountToDelete(null);
    }
  };

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
          <h2 className="fw-bold text-dark mb-1">
            <i className="bi bi-people-fill text-primary me-2"></i>Quản Lý Tài Khoản (Admin)
          </h2>
          <p className="text-muted small mb-0">Quản lý danh sách tài khoản Admin và Staff trong hệ thống</p>
        </div>
        <button className="btn btn-primary shadow-sm" onClick={handleOpenCreate}>
          <i className="bi bi-person-plus-fill me-1"></i>Thêm tài khoản mới
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
            placeholder="Tìm kiếm tài khoản theo tên hoặc email..."
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
                <th>Tên người dùng</th>
                <th>Email</th>
                <th>Vai trò (Role)</th>
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
              ) : accounts.length === 0 ? (
                <tr>
                  <td colSpan="5" className="text-center py-4 text-muted">Không tìm thấy tài khoản nào.</td>
                </tr>
              ) : (
                accounts.map((acc) => (
                  <tr key={acc.accountId}>
                    <td className="fw-bold">#{acc.accountId}</td>
                    <td className="fw-semibold text-dark">{acc.accountName}</td>
                    <td>{acc.accountEmail}</td>
                    <td>
                      <span className={`badge ${acc.accountRole === 1 ? 'bg-danger' : 'bg-primary'}`}>
                        {acc.accountRole === 1 ? 'Admin (1)' : 'Staff (2)'}
                      </span>
                    </td>
                    <td className="text-end">
                      <button
                        className="btn btn-outline-secondary btn-sm me-2"
                        title="Chỉnh sửa"
                        onClick={() => handleOpenEdit(acc)}
                      >
                        <i className="bi bi-pencil-square"></i>
                      </button>
                      <button
                        className="btn btn-outline-danger btn-sm"
                        title="Xóa tài khoản"
                        onClick={() => handleOpenDelete(acc)}
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
        title={isEditing ? 'Chỉnh sửa tài khoản' : 'Thêm mới tài khoản'}
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleSave}>
          <div className="mb-3">
            <label className="form-label fw-semibold">Họ và tên <span className="text-danger">*</span></label>
            <input
              type="text"
              className="form-control"
              required
              value={formData.accountName}
              onChange={(e) => setFormData({ ...formData, accountName: e.target.value })}
            />
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Email <span className="text-danger">*</span></label>
            <input
              type="email"
              className="form-control"
              required
              value={formData.accountEmail}
              onChange={(e) => setFormData({ ...formData, accountEmail: e.target.value })}
            />
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Vai trò <span className="text-danger">*</span></label>
            <select
              className="form-select"
              value={formData.accountRole}
              onChange={(e) => setFormData({ ...formData, accountRole: Number(e.target.value) })}
            >
              <option value={1}>Admin (1)</option>
              <option value={2}>Staff (2)</option>
            </select>
          </div>

          <div className="mb-4">
            <label className="form-label fw-semibold">
              Mật khẩu {isEditing ? '(để trống nếu giữ nguyên)' : <span className="text-danger">*</span>}
            </label>
            <input
              type="password"
              className="form-control"
              required={!isEditing}
              value={formData.accountPassword}
              onChange={(e) => setFormData({ ...formData, accountPassword: e.target.value })}
            />
          </div>

          <div className="d-flex justify-content-end gap-2 border-top pt-3">
            <button type="button" className="btn btn-secondary" onClick={() => setIsModalOpen(false)}>
              Hủy
            </button>
            <button type="submit" className="btn btn-primary px-4">
              {isEditing ? 'Lưu thay đổi' : 'Tạo mới'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <ConfirmModal
        isOpen={!!deleteId}
        title="Xác nhận xóa tài khoản"
        message={`Bạn có chắc chắn muốn xóa tài khoản "${accountToDelete?.accountName}" (${accountToDelete?.accountEmail}) không? Hành động này không thể hoàn tác.`}
        onConfirm={handleConfirmDelete}
        onCancel={() => { setDeleteId(null); setAccountToDelete(null); }}
      />
    </div>
  );
};

