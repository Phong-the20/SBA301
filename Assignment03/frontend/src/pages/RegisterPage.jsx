/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authApi } from '../api/authApi';

export const RegisterPage = () => {
  const [formData, setFormData] = useState({
    customerFullName: '',
    telephone: '',
    emailAddress: '',
    customerBirthday: '',
    password: '',
    confirmPassword: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (formData.password !== formData.confirmPassword) {
      setError('Mật khẩu xác nhận không khớp!');
      return;
    }

    setLoading(true);
    try {
      const payload = {
        customerFullName: formData.customerFullName,
        telephone: formData.telephone,
        emailAddress: formData.emailAddress,
        customerBirthday: formData.customerBirthday || null,
        password: formData.password
      };
      const response = await authApi.register(payload);
      login(response);
      navigate('/customer/book');
    } catch (err) {
      setError(err.message || 'Đăng ký không thành công.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-7 col-lg-6">
          <div className="card shadow border-0 rounded-4 p-4 bg-white">
            <div className="text-center mb-4">
              <span className="fs-1">📝</span>
              <h3 className="fw-bold text-dark mt-2">Đăng Ký Tài Khoản Khách Hàng</h3>
              <p className="text-muted small">Tạo tài khoản để đặt phòng khách sạn và quản lý kỳ nghỉ</p>
            </div>

            {error && (
              <div className="alert alert-danger py-2 small d-flex align-items-center gap-2">
                <i className="bi bi-exclamation-circle-fill"></i>
                <div>{error}</div>
              </div>
            )}

            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label fw-semibold">Họ và tên <span className="text-danger">*</span></label>
                <input
                  type="text"
                  className="form-control"
                  required
                  placeholder="VD: Nguyen Van An"
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
                    placeholder="VD: 0901234567"
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

              <div className="mb-3">
                <label className="form-label fw-semibold">Email <span className="text-danger">*</span></label>
                <input
                  type="email"
                  className="form-control"
                  required
                  placeholder="name@gmail.com"
                  value={formData.emailAddress}
                  onChange={(e) => setFormData({ ...formData, emailAddress: e.target.value })}
                />
              </div>

              <div className="row g-3 mb-4">
                <div className="col-md-6">
                  <label className="form-label fw-semibold">Mật khẩu <span className="text-danger">*</span></label>
                  <input
                    type="password"
                    className="form-control"
                    required
                    placeholder="Tối thiểu 6 ký tự"
                    value={formData.password}
                    onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  />
                </div>
                <div className="col-md-6">
                  <label className="form-label fw-semibold">Xác nhận mật khẩu <span className="text-danger">*</span></label>
                  <input
                    type="password"
                    className="form-control"
                    required
                    placeholder="Nhập lại mật khẩu"
                    value={formData.confirmPassword}
                    onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
                  />
                </div>
              </div>

              <button
                type="submit"
                className="btn btn-warning w-100 py-2 fw-bold text-dark shadow-sm"
                disabled={loading}
              >
                {loading ? 'Đang tạo tài khoản...' : 'Hoàn Tất Đăng Ký'}
              </button>
            </form>

            <div className="mt-3 text-center">
              <span className="text-muted small">Đã có tài khoản? </span>
              <Link to="/login" className="small fw-bold text-primary">
                Đăng nhập ngay
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
