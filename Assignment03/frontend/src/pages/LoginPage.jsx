/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authApi } from '../api/authApi';

export const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await authApi.login(email, password);
      login(response);
      if (response.role === 'STAFF') {
        navigate('/staff/rooms');
      } else {
        navigate('/customer/book');
      }
    } catch (err) {
      setError(err.message || 'Email hoặc mật khẩu không chính xác!');
    } finally {
      setLoading(false);
    }
  };

  const handleQuickLogin = (quickEmail, quickPassword) => {
    setEmail(quickEmail);
    setPassword(quickPassword);
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-6 col-lg-5">
          <div className="card shadow border-0 rounded-4 p-4 bg-white">
            <div className="text-center mb-4">
              <span className="fs-1">🏨</span>
              <h3 className="fw-bold text-dark mt-2">Đăng Nhập Khách Sạn</h3>
              <p className="text-muted small">FU Mini Hotel Management System (Spring Security + JWT)</p>
            </div>

            {error && (
              <div className="alert alert-danger py-2 small d-flex align-items-center gap-2">
                <i className="bi bi-exclamation-circle-fill"></i>
                <div>{error}</div>
              </div>
            )}

            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label fw-semibold">Email tài khoản</label>
                <div className="input-group">
                  <span className="input-group-text"><i className="bi bi-envelope"></i></span>
                  <input
                    type="email"
                    className="form-control"
                    placeholder="email@hotel.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </div>
              </div>

              <div className="mb-4">
                <label className="form-label fw-semibold">Mật khẩu</label>
                <div className="input-group">
                  <span className="input-group-text"><i className="bi bi-lock"></i></span>
                  <input
                    type="password"
                    className="form-control"
                    placeholder="Nhập mật khẩu"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                </div>
              </div>

              <button
                type="submit"
                className="btn btn-warning w-100 py-2 fw-bold text-dark shadow-sm"
                disabled={loading}
              >
                {loading ? 'Đang xác thực bảo mật JWT...' : 'Đăng Nhập'}
              </button>
            </form>

            <div className="mt-3 text-center">
              <span className="text-muted small">Chưa có tài khoản? </span>
              <Link to="/register" className="small fw-bold text-primary">
                Đăng ký thành viên mới
              </Link>
            </div>

            <div className="mt-4 pt-3 border-top text-center">
              <p className="text-muted small mb-2">Tài khoản demo theo yêu cầu đề bài:</p>
              <div className="d-flex flex-column gap-2">
                <button
                  type="button"
                  className="btn btn-sm btn-outline-danger"
                  onClick={() => handleQuickLogin('admin@hotel.com', 'admin123')}
                >
                  <i className="bi bi-shield-lock-fill me-1"></i>Staff (application.properties): admin@hotel.com
                </button>
                <button
                  type="button"
                  className="btn btn-sm btn-outline-primary"
                  onClick={() => handleQuickLogin('an.nguyen@gmail.com', '123456')}
                >
                  <i className="bi bi-person-fill me-1"></i>Customer (Khách hàng): an.nguyen@gmail.com
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
