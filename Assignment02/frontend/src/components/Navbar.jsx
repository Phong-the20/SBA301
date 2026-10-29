/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export const Navbar = () => {
  const { user, logout, isAdmin, isStaff } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
      <div className="container">
        <Link className="navbar-brand d-flex align-items-center gap-2" to="/">
          <span className="fs-3">📰</span>
          <span className="fw-bold tracking-tight text-white">FUNews<span className="text-warning">Management</span></span>
        </Link>

        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto">
            <li className="nav-item">
              <Link className="nav-link text-light" to="/"><i className="bi bi-house-door me-1"></i>Trang chủ Tin Tức</Link>
            </li>

            {/* Admin Function */}
            {isAdmin && (
              <li className="nav-item">
                <Link className="nav-link text-warning fw-semibold" to="/admin/accounts">
                  <i className="bi bi-people-fill me-1"></i>Quản lý Tài Khoản (Admin)
                </Link>
              </li>
            )}

            {/* Staff Functions */}
            {isStaff && (
              <>
                <li className="nav-item">
                  <Link className="nav-link text-info fw-semibold" to="/staff/categories">
                    <i className="bi bi-tags-fill me-1"></i>Quản lý Danh Mục
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link text-info fw-semibold" to="/staff/news">
                    <i className="bi bi-newspaper me-1"></i>Quản lý Bài Viết
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link text-info fw-semibold" to="/staff/history">
                    <i className="bi bi-clock-history me-1"></i>Lịch Sử Đăng Bài
                  </Link>
                </li>
              </>
            )}
          </ul>

          <div className="d-flex align-items-center gap-2">
            {user ? (
              <div className="dropdown">
                <button className="btn btn-outline-light dropdown-toggle d-flex align-items-center gap-2" data-bs-toggle="dropdown">
                  <i className="bi bi-person-circle"></i>
                  <span>{user.accountName}</span>
                  <span className={`badge ${isAdmin ? 'bg-danger' : 'bg-primary'}`}>
                    {isAdmin ? 'ADMIN' : 'STAFF'}
                  </span>
                </button>
                <ul className="dropdown-menu dropdown-menu-end shadow">
                  <li className="dropdown-item-text text-muted small">
                    {user.accountEmail}
                  </li>
                  <li><hr className="dropdown-divider" /></li>
                  <li>
                    <Link className="dropdown-item" to="/profile">
                      <i className="bi bi-person me-2"></i>Hồ sơ cá nhân
                    </Link>
                  </li>
                  <li>
                    <button className="dropdown-item text-danger" onClick={handleLogout}>
                      <i className="bi bi-box-arrow-right me-2"></i>Đăng xuất
                    </button>
                  </li>
                </ul>
              </div>
            ) : (
              <Link className="btn btn-warning fw-semibold px-3" to="/login">
                <i className="bi bi-box-arrow-in-right me-1"></i>Đăng nhập
              </Link>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};
