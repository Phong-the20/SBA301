/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React, { useEffect, useState } from 'react';
import { customerApi } from '../api/customerApi';
import { useAuth } from '../context/AuthContext';

export const ProfilePage = () => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    customerFullName: '',
    telephone: '',
    customerBirthday: '',
    password: '',
    confirmPassword: ''
  });
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    customerApi.getProfile()
      .then((data) => {
        setFormData({
          customerFullName: data.customerFullName || '',
          telephone: data.telephone || '',
          customerBirthday: data.customerBirthday || '',
          password: '',
          confirmPassword: ''
        });
      })
      .catch((err) => console.error(err));
  }, []);

  const handleUpdate = async (e) => {
    e.preventDefault();
    setMessage('');
    setError('');

    if (formData.password && formData.password !== formData.confirmPassword) {
      setError('Mật khẩu xác nhận không khớp!');
      return;
    }

    try {
      setLoading(true);
      await customerApi.updateProfile({
        customerFullName: formData.customerFullName,
        telephone: formData.telephone,
        customerBirthday: formData.customerBirthday || null,
        password: formData.password || undefined
      });
      setMessage('Cập nhật hồ sơ cá nhân thành công!');
      setFormData((prev) => ({ ...prev, password: '', confirmPassword: '' }));
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-7 col-lg-6">
          <div className="card shadow-sm border-0 rounded-4 p-4 bg-white">
            <h4 className="fw-bold mb-4 text-dark border-bottom pb-2">
              <i className="bi bi-person-circle text-primary me-2"></i>Hồ Sơ Khách Hàng
            </h4>

            {message && (
              <div className="alert alert-success d-flex align-items-center gap-2">
                <i className="bi bi-check-circle-fill"></i>
                <div>{message}</div>
              </div>
            )}

            {error && (
              <div className="alert alert-danger d-flex align-items-center gap-2">
                <i className="bi bi-exclamation-triangle-fill"></i>
                <div>{error}</div>
              </div>
            )}

            <form onSubmit={handleUpdate}>
              <div className="mb-3">
                <label className="form-label text-muted small">Email tài khoản</label>
                <input type="text" className="form-control" value={user?.email || ''} disabled />
              </div>

              <div className="mb-3">
                <label className="form-label fw-semibold">Họ và tên</label>
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
                  <label className="form-label fw-semibold">Số điện thoại</label>
                  <input
                    type="tel"
                    className="form-control"
                    value={formData.telephone}
                    onChange={(e) => setFormData({ ...formData, telephone: e.target.value })}
                  />
                </div>
                <div className="col-md-6">
                  <label className="form-label fw-semibold">Ngày sinh</label>
                  <input
                    type="date"
                    className="form-control"
                    value={formData.customerBirthday}
                    onChange={(e) => setFormData({ ...formData, customerBirthday: e.target.value })}
                  />
                </div>
              </div>

              <hr className="my-4" />
              <h6 className="fw-bold text-secondary mb-3">Đổi mật khẩu mới (tùy chọn)</h6>

              <div className="mb-3">
                <label className="form-label small">Mật khẩu mới</label>
                <input
                  type="password"
                  className="form-control"
                  placeholder="Để trống nếu không đổi"
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                />
              </div>

              <div className="mb-4">
                <label className="form-label small">Xác nhận mật khẩu mới</label>
                <input
                  type="password"
                  className="form-control"
                  placeholder="Nhập lại mật khẩu"
                  value={formData.confirmPassword}
                  onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
                />
              </div>

              <button type="submit" className="btn btn-primary w-100 py-2 fw-semibold" disabled={loading}>
                {loading ? 'Đang lưu...' : 'Lưu Thay Đổi'}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};
