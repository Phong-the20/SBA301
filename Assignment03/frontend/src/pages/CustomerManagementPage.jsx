/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React, { useEffect, useState } from 'react';
import { customerApi } from '../api/customerApi';
import { Modal } from '../components/Modal';
import { ConfirmModal } from '../components/ConfirmModal';

export const CustomerManagementPage = () => {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [errorAlert, setErrorAlert] = useState('');
  const [successAlert, setSuccessAlert] = useState('');

  // Modal State for Create / Edit
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    customerID: null,
    customerFullName: '',
    telephone: '',
    emailAddress: '',
    customerBirthday: '',
    customerStatus: 1,
    password: ''
  });

  // Modal State for Delete Confirmation
  const [deleteId, setDeleteId] = useState(null);
  const [customerToDelete, setCustomerToDelete] = useState(null);

  const fetchCustomers = async () => {
    try {
      setLoading(true);
      const data = await customerApi.getAll(keyword);
      setCustomers(data || []);
    } catch (err) {
      setErrorAlert(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCustomers();
  }, [keyword]);

  const handleOpenCreate = () => {
    setIsEditing(false);
    setFormData({
      customerID: null,
      customerFullName: '',
      telephone: '',
      emailAddress: '',
      customerBirthday: '',
      customerStatus: 1,
      password: ''
    });
    setErrorAlert('');
    setIsModalOpen(true);
  };

  const handleOpenEdit = (c) => {
    setIsEditing(true);
    setFormData({
      customerID: c.customerID,
      customerFullName: c.customerFullName,
      telephone: c.telephone || '',
      emailAddress: c.emailAddress,
      customerBirthday: c.customerBirthday || '',
      customerStatus: c.customerStatus,
      password: ''
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
        await customerApi.update(formData.customerID, formData);
        setSuccessAlert('Cập nhật thông tin khách hàng thành công!');
      } else {
        await customerApi.create(formData);
        setSuccessAlert('Thêm khách hàng mới thành công!');
      }
      setIsModalOpen(false);
      fetchCustomers();
    } catch (err) {
      setErrorAlert(err.message);
    }
  };

  const handleOpenDelete = (c) => {
    setDeleteId(c.customerID);
    setCustomerToDelete(c);
    setErrorAlert('');
  };

  const handleConfirmDelete = async () => {
    if (!deleteId) return;
    setErrorAlert('');
    setSuccessAlert('');
    try {
      await customerApi.delete(deleteId);
      setSuccessAlert('Xóa khách hàng thành công (nếu khách hàng có lịch sử đặt phòng, hệ thống tự động đổi trạng thái sang Inactive)!');
      setDeleteId(null);
      setCustomerToDelete(null);
      fetchCustomers();
    } catch (err) {
      setErrorAlert(err.message);
      setDeleteId(null);
      setCustomerToDelete(null);
    }
  };

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
          <h2 className="fw-bold text-dark mb-1">
            <i className="bi bi-people-fill text-warning me-2"></i>Quản Lý Khách Hàng (Staff)
          </h2>
          <p className="text-muted small mb-0">Quản lý hồ sơ khách hàng, số điện thoại và thông tin tài khoản</p>
        </div>
        <button className="btn btn-warning fw-bold shadow-sm" onClick={handleOpenCreate}>
          <i className="bi bi-person-plus-fill me-1"></i>Thêm khách hàng mới
        </button>
      </div>

      {/* Notifications */}
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

      {/* Search Input */}
      <div className="card shadow-sm border-0 mb-4 p-3 bg-white">
        <div className="input-group">
          <span className="input-group-text bg-light"><i className="bi bi-search"></i></span>
          <input
            type="text"
            className="form-control"
            placeholder="Tìm kiếm khách hàng theo tên hoặc email..."
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
                <th>Họ và tên</th>
                <th>Email</th>
                <th>Số điện thoại</th>
                <th>Ngày sinh</th>
                <th>Trạng thái</th>
                <th className="text-end" style={{ width: '160px' }}>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="7" className="text-center py-4 text-muted">
                    <div className="spinner-border spinner-border-sm me-2"></div> Đang tải dữ liệu...
                  </td>
                </tr>
              ) : customers.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-4 text-muted">Không tìm thấy khách hàng nào.</td>
                </tr>
              ) : (
                customers.map((c) => (
                  <tr key={c.customerID}>
                    <td className="fw-bold">#{c.customerID}</td>
                    <td className="fw-semibold text-dark">{c.customerFullName}</td>
                    <td>{c.emailAddress}</td>
                    <td>{c.telephone || '—'}</td>
                    <td>{c.customerBirthday || '—'}</td>
                    <td>
                      <span className={`badge ${c.customerStatus === 1 ? 'bg-success' : 'bg-secondary'}`}>
                        {c.customerStatus === 1 ? 'Active (1)' : 'Inactive (0)'}
                      </span>
                    </td>
                    <td className="text-end">
                      <button
                        className="btn btn-outline-secondary btn-sm me-2"
                        title="Chỉnh sửa"
                        onClick={() => handleOpenEdit(c)}
                      >
                        <i className="bi bi-pencil-square"></i>
                      </button>
                      <button
                        className="btn btn-outline-danger btn-sm"
                        title="Xóa"
                        onClick={() => handleOpenDelete(c)}
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

      {/* Modal for Create/Edit */}
      <Modal
        isOpen={isModalOpen}
        title={isEditing ? 'Chỉnh sửa khách hàng' : 'Thêm mới khách hàng'}
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleSave}>
          <div className="mb-3">
            <label className="form-label fw-semibold">Họ và tên <span className="text-danger">*</span></label>
            <input
              type="text"
              className="form-control"
              required
              value={formData.customerFullName}
              onChange={(e) => setFormData({ ...formData, customerFullName: e.target.value })}
            />
          </div>

          <div className="row g-3 mb-3">
            <div className="col-md-6">
              <label className="form-label fw-semibold">Email <span className="text-danger">*</span></label>
              <input
                type="email"
                className="form-control"
                required
                value={formData.emailAddress}
                onChange={(e) => setFormData({ ...formData, emailAddress: e.target.value })}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label fw-semibold">Số điện thoại</label>
              <input
                type="tel"
                className="form-control"
                value={formData.telephone}
                onChange={(e) => setFormData({ ...formData, telephone: e.target.value })}
              />
            </div>
          </div>

          <div className="row g-3 mb-3">
            <div className="col-md-6">
              <label className="form-label fw-semibold">Ngày sinh</label>
              <input
                type="date"
                className="form-control"
                value={formData.customerBirthday}
                onChange={(e) => setFormData({ ...formData, customerBirthday: e.target.value })}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label fw-semibold">Trạng thái <span className="text-danger">*</span></label>
              <select
                className="form-select"
                value={formData.customerStatus}
                onChange={(e) => setFormData({ ...formData, customerStatus: Number(e.target.value) })}
              >
                <option value={1}>Active (Hoạt động - 1)</option>
                <option value={0}>Inactive (Khóa - 0)</option>
              </select>
            </div>
          </div>

          <div className="mb-4">
            <label className="form-label fw-semibold">
              Mật khẩu {isEditing ? '(để trống nếu không đổi)' : <span className="text-danger">*</span>}
            </label>
            <input
              type="password"
              className="form-control"
              required={!isEditing}
              value={formData.password}
              onChange={(e) => setFormData({ ...formData, password: e.target.value })}
            />
          </div>

          <div className="d-flex justify-content-end gap-2 border-top pt-3">
            <button type="button" className="btn btn-secondary" onClick={() => setIsModalOpen(false)}>
              Hủy
            </button>
            <button type="submit" className="btn btn-warning fw-bold px-4">
              {isEditing ? 'Lưu thay đổi' : 'Tạo mới'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <ConfirmModal
        isOpen={!!deleteId}
        title="Xác nhận xóa khách hàng"
        message={`Bạn có chắc muốn xóa khách hàng "${customerToDelete?.customerFullName}" không?`}
        onConfirm={handleConfirmDelete}
        onCancel={() => { setDeleteId(null); setCustomerToDelete(null); }}
      />
    </div>
  );
};
