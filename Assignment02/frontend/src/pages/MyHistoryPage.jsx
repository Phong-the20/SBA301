/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import React, { useEffect, useState } from 'react';
import { newsApi } from '../api/newsApi';
import { useAuth } from '../context/AuthContext';

export const MyHistoryPage = () => {
  const { user } = useAuth();
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user?.accountId) {
      newsApi.getMyHistory(user.accountId)
        .then((data) => setHistory(data || []))
        .catch((err) => console.error(err))
        .finally(() => setLoading(false));
    }
  }, [user]);

  return (
    <div className="container py-4">
      <div className="mb-4 pb-2 border-bottom">
        <h2 className="fw-bold text-dark mb-1">
          <i className="bi bi-clock-history text-info me-2"></i>Lịch Sử Đăng Bài Của Tôi
        </h2>
        <p className="text-muted small mb-0">
          Danh sách toàn bộ các bài viết do tài khoản <strong>{user?.accountName}</strong> khởi tạo
        </p>
      </div>

      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-info" role="status"></div>
          <p className="mt-2 text-muted">Đang tải lịch sử bài viết...</p>
        </div>
      ) : history.length === 0 ? (
        <div className="text-center py-5 bg-white rounded shadow-sm">
          <i className="bi bi-journal-x text-muted" style={{ fontSize: '3rem' }}></i>
          <h5 className="mt-3 text-muted">Bạn chưa đăng bài viết nào</h5>
        </div>
      ) : (
        <div className="card shadow-sm border-0">
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-light">
                <tr>
                  <th style={{ width: '80px' }}>ID</th>
                  <th>Tiêu đề bài viết</th>
                  <th>Chuyên mục</th>
                  <th>Ngày tạo</th>
                  <th>Cập nhật lần cuối</th>
                  <th>Trạng thái</th>
                </tr>
              </thead>
              <tbody>
                {history.map((item) => (
                  <tr key={item.newsArticleId}>
                    <td className="fw-bold">#{item.newsArticleId}</td>
                    <td>
                      <div className="fw-semibold text-dark">{item.newsTitle}</div>
                      <small className="text-muted">{item.headline}</small>
                    </td>
                    <td>
                      <span className="badge bg-light text-primary border">{item.categoryName}</span>
                    </td>
                    <td><small>{item.createdDate ? new Date(item.createdDate).toLocaleString('vi-VN') : ''}</small></td>
                    <td><small>{item.modifiedDate ? new Date(item.modifiedDate).toLocaleString('vi-VN') : ''}</small></td>
                    <td>
                      <span className={`badge ${item.newsStatus === 1 ? 'bg-success' : 'bg-secondary'}`}>
                        {item.newsStatus === 1 ? 'Đã xuất bản (1)' : 'Bản nháp (0)'}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};

