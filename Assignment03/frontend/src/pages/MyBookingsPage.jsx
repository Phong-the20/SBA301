/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React, { useEffect, useState } from 'react';
import { bookingApi } from '../api/bookingApi';
import { Modal } from '../components/Modal';

export const MyBookingsPage = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedBooking, setSelectedBooking] = useState(null);

  useEffect(() => {
    bookingApi.getMyHistory()
      .then((data) => setBookings(data || []))
      .catch((err) => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  const getStatusBadge = (status) => {
    switch (status) {
      case 1:
        return <span className="badge bg-primary">Đã xác nhận (1)</span>;
      case 2:
        return <span className="badge bg-success">Hoàn thành (2)</span>;
      case 0:
        return <span className="badge bg-danger">Đã hủy (0)</span>;
      default:
        return <span className="badge bg-secondary">Khác</span>;
    }
  };

  return (
    <div className="container py-4">
      <div className="mb-4 pb-2 border-bottom">
        <h2 className="fw-bold text-dark mb-1">
          <i className="bi bi-clock-history text-info me-2"></i>Lịch Sử Đặt Phòng Của Tôi
        </h2>
        <p className="text-muted small mb-0">Theo dõi trạng thái các kỳ nghỉ và phòng khách sạn bạn đã đặt</p>
      </div>

      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-info" role="status"></div>
          <p className="mt-2 text-muted">Đang tải lịch sử đặt phòng...</p>
        </div>
      ) : bookings.length === 0 ? (
        <div className="text-center py-5 bg-white rounded-3 shadow-sm">
          <i className="bi bi-calendar-x text-muted" style={{ fontSize: '3rem' }}></i>
          <h5 className="mt-3 text-muted">Bạn chưa có đơn đặt phòng nào</h5>
        </div>
      ) : (
        <div className="card shadow-sm border-0">
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-light">
                <tr>
                  <th style={{ width: '90px' }}>Mã đơn</th>
                  <th>Ngày đặt</th>
                  <th>Số lượng phòng</th>
                  <th>Tổng chi phí</th>
                  <th>Trạng thái</th>
                  <th className="text-end" style={{ width: '140px' }}>Chi tiết</th>
                </tr>
              </thead>
              <tbody>
                {bookings.map((b) => (
                  <tr key={b.bookingReservationID}>
                    <td className="fw-bold">#{b.bookingReservationID}</td>
                    <td>{b.bookingDate ? new Date(b.bookingDate).toLocaleString('vi-VN') : ''}</td>
                    <td>{b.details?.length || 0} phòng</td>
                    <td className="fw-bold text-danger">
                      {Number(b.totalPrice).toLocaleString('vi-VN')} đ
                    </td>
                    <td>{getStatusBadge(b.bookingStatus)}</td>
                    <td className="text-end">
                      <button
                        className="btn btn-outline-primary btn-sm"
                        onClick={() => setSelectedBooking(b)}
                      >
                        <i className="bi bi-eye-fill me-1"></i>Xem phòng
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Modal for Details */}
      <Modal
        isOpen={!!selectedBooking}
        title={`Chi tiết đơn đặt phòng #${selectedBooking?.bookingReservationID}`}
        onClose={() => setSelectedBooking(null)}
      >
        {selectedBooking && (
          <div>
            <div className="table-responsive">
              <table className="table table-bordered table-sm align-middle">
                <thead className="table-light">
                  <tr>
                    <th>Số phòng</th>
                    <th>Loại phòng</th>
                    <th>Từ ngày</th>
                    <th>Đến ngày</th>
                    <th className="text-end">Chi phí</th>
                  </tr>
                </thead>
                <tbody>
                  {selectedBooking.details?.map((item, idx) => (
                    <tr key={idx}>
                      <td className="fw-bold">Phòng {item.roomNumber}</td>
                      <td>{item.roomTypeName}</td>
                      <td>{item.startDate}</td>
                      <td>{item.endDate}</td>
                      <td className="text-end fw-semibold text-danger">
                        {Number(item.actualPrice).toLocaleString('vi-VN')} đ
                      </td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr className="table-light">
                    <td colSpan="4" className="fw-bold text-end">Tổng cộng:</td>
                    <td className="text-end fw-bold text-danger fs-6">
                      {Number(selectedBooking.totalPrice).toLocaleString('vi-VN')} đ
                    </td>
                  </tr>
                </tfoot>
              </table>
            </div>

            <div className="d-flex justify-content-end mt-3">
              <button className="btn btn-secondary" onClick={() => setSelectedBooking(null)}>
                Đóng
              </button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
};
