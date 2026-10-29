/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export const Navbar = () => {
  const { user, logout, isStaff, isCustomer } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
      <div className="container">
        <Link className="navbar-brand d-flex align-items-center gap-2" to="/">
          <span className="fs-3">🏨</span>
          <span className="fw-bold tracking-tight text-white">
            FU<span className="text-warning">MiniHotel</span>System
          </span>
        </Link>

        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#hotelNavbar">
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="hotelNavbar">
          <ul className="navbar-nav me-auto">
            <li className="nav-item">
              <Link className="nav-link text-light" to="/">
                <i className="bi bi-door-open me-1"></i>Xem Phòng (Public)
              </Link>
            </li>

            {/* Staff Functions */}
            {isStaff && (
              <>
                <li className="nav-item">
                  <Link className="nav-link text-warning fw-semibold" to="/staff/rooms">
                    <i className="bi bi-grid-3x3-gap-fill me-1"></i>Quản lý Phòng
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link text-warning fw-semibold" to="/staff/customers">
                    <i className="bi bi-people-fill me-1"></i>Quản lý Khách Hàng
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link text-warning fw-semibold" to="/staff/bookings">
                    <i className="bi bi-calendar-check-fill me-1"></i>Quản lý Đặt Phòng
                  </Link>
                </li>
              </>
            )}

            {/* Customer Functions */}
            {isCustomer && (
              <>
                <li className="nav-item">
                  <Link className="nav-link text-info fw-semibold" to="/customer/book">
                    <i className="bi bi-bookmark-plus-fill me-1"></i>Đặt Phòng Online
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link text-info fw-semibold" to="/customer/my-bookings">
                    <i className="bi bi-clock-history me-1"></i>Lịch Sử Đặt Phòng
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
                  <span>{user.name}</span>
                  <span className={`badge ${isStaff ? 'bg-warning text-dark' : 'bg-info text-dark'}`}>
                    {user.role}
                  </span>
                </button>
                <ul className="dropdown-menu dropdown-menu-end shadow">
                  <li className="dropdown-item-text text-muted small">{user.email}</li>
                  <li><hr className="dropdown-divider" /></li>
                  {isCustomer && (
                    <li>
                      <Link className="dropdown-item" to="/profile">
                        <i className="bi bi-person me-2"></i>Hồ sơ cá nhân
                      </Link>
                    </li>
                  )}
                  <li>
                    <button className="dropdown-item text-danger" onClick={handleLogout}>
                      <i className="bi bi-box-arrow-right me-2"></i>Đăng xuất
                    </button>
                  </li>
                </ul>
              </div>
            ) : (
              <div className="d-flex gap-2">
                <Link className="btn btn-outline-light btn-sm px-3" to="/login">
                  Đăng nhập
                </Link>
                <Link className="btn btn-warning btn-sm px-3 text-dark fw-semibold" to="/register">
                  Đăng ký
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};
