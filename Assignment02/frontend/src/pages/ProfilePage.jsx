/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { accountApi } from '../api/accountApi';

export const ProfilePage = () => {
  const { user, login } = useAuth();
  const [name, setName] = useState(user?.accountName || '');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleUpdate = async (e) => {
    e.preventDefault();
    setMessage('');
    setError('');

    if (password && password !== confirmPassword) {
      setError('Mật khẩu xác nhận không khớp!');
      return;
    }

    try {
      setLoading(true);
      const updated = await accountApi.updateProfile(user.accountId, {
        accountName: name,
        accountPassword: password || undefined
      });
      login({ ...user, accountName: updated.accountName });
      setMessage('Cập nhật hồ sơ thành công!');
      setPassword('');
      setConfirmPassword('');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-8 col-lg-6">
          <div className="card shadow-sm border-0 rounded-3 p-4 bg-white">
            <h4 className="fw-bold mb-4 text-dark border-bottom pb-2">
              <i className="bi bi-person-circle text-primary me-2"></i>Hồ Sơ Cá Nhân
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
                <label className="form-label text-muted small">Mã tài khoản (ID)</label>
                <input type="text" className="form-control" value={user?.accountId || ''} disabled />
              </div>

              <div className="mb-3">
                <label className="form-label text-muted small">Email đăng nhập</label>
                <input type="email" className="form-control" value={user?.accountEmail || ''} disabled />
              </div>

              <div className="mb-3">
                <label className="form-label text-muted small">Vai trò hệ thống</label>
                <input
                  type="text"
                  className="form-control"
                  value={user?.accountRole === 1 ? 'Quản trị viên (Admin - 1)' : 'Nhân viên biên tập (Staff - 2)'}
                  disabled
                />
              </div>

              <div className="mb-3">
                <label className="form-label fw-semibold">Họ và tên hiển thị</label>
                <input
                  type="text"
                  className="form-control"
                  required
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
              </div>

              <hr className="my-4" />
              <h6 className="fw-bold text-secondary mb-3">Đổi mật khẩu (tùy chọn)</h6>

              <div className="mb-3">
                <label className="form-label small">Mật khẩu mới</label>
                <input
                  type="password"
                  className="form-control"
                  placeholder="Để trống nếu không muốn đổi"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>

              <div className="mb-4">
                <label className="form-label small">Nhập lại mật khẩu mới</label>
                <input
                  type="password"
                  className="form-control"
                  placeholder="Xác nhận lại mật khẩu mới"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                />
              </div>

              <button type="submit" className="btn btn-primary w-100 py-2 fw-semibold" disabled={loading}>
                {loading ? 'Đang lưu...' : 'Lưu Thay Đổi Hồ Sơ'}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

