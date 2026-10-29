/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React, { useEffect, useState } from 'react';
import { bookingApi } from '../api/bookingApi';
import { Modal } from '../components/Modal';

export const BookingManagementPage = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedBooking, setSelectedBooking] = useState(null);
  const [message, setMessage] = useState('');

  const fetchBookings = async () => {
    try {
      setLoading(true);
      const data = await bookingApi.getAll();
      setBookings(data || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBookings();
  }, []);

  const handleUpdateStatus = async (bookingId, newStatus) => {
    try {
      await bookingApi.updateStatus(bookingId, newStatus);
      setMessage(`Đã cập nhật trạng thái đơn đặt phòng #${bookingId} thành công!`);
      fetchBookings();
      if (selectedBooking && selectedBooking.bookingReservationID === bookingId) {
        setSelectedBooking({ ...selectedBooking, bookingStatus: newStatus });
      }
    } catch (err) {
      alert(err.message);
    }
  };

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
      <div className="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
          <h2 className="fw-bold text-dark mb-1">
            <i className="bi bi-calendar-check-fill text-warning me-2"></i>Quản Lý Đặt Phòng (Staff)
          </h2>
          <p className="text-muted small mb-0">Theo dõi toàn bộ đơn đặt phòng và chi tiết phòng thuê</p>
        </div>
      </div>

      {message && (
        <div className="alert alert-success alert-dismissible fade show d-flex align-items-center gap-2">
          <i className="bi bi-check-circle-fill fs-5"></i>
          <div>{message}</div>
          <button type="button" className="btn-close" onClick={() => setMessage('')}></button>
        </div>
      )}

      <div className="card shadow-sm border-0">
        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0">
            <thead className="table-light">
              <tr>
                <th style={{ width: '80px' }}>ID</th>
                <th>Khách hàng</th>
                <th>Ngày đặt</th>
                <th>Tổng thanh toán</th>
                <th>Trạng thái</th>
                <th className="text-end" style={{ width: '220px' }}>Thao tác trạng thái</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="6" className="text-center py-4 text-muted">
                    <div className="spinner-border spinner-border-sm me-2"></div> Đang tải danh sách đặt phòng...
                  </td>
                </tr>
              ) : bookings.length === 0 ? (
                <tr>
                  <td colSpan="6" className="text-center py-4 text-muted">Chưa có giao dịch đặt phòng nào.</td>
                </tr>
              ) : (
                bookings.map((b) => (
                  <tr key={b.bookingReservationID}>
                    <td className="fw-bold">#{b.bookingReservationID}</td>
                    <td>
                      <div className="fw-semibold text-dark">{b.customerFullName}</div>
                      <small className="text-muted">{b.customerEmail}</small>
                    </td>
                    <td><small>{b.bookingDate ? new Date(b.bookingDate).toLocaleString('vi-VN') : ''}</small></td>
                    <td className="fw-bold text-danger">
                      {Number(b.totalPrice).toLocaleString('vi-VN')} đ
                    </td>
                    <td>{getStatusBadge(b.bookingStatus)}</td>
                    <td className="text-end">
                      <button
                        className="btn btn-outline-info btn-sm me-2"
                        title="Xem chi tiết các phòng"
                        onClick={() => setSelectedBooking(b)}
                      >
                        <i className="bi bi-eye-fill me-1"></i>Chi tiết ({b.details?.length || 0})
                      </button>

                      {b.bookingStatus === 1 && (
                        <>
                          <button
                            className="btn btn-success btn-sm me-1"
                            title="Đánh dấu hoàn thành"
                            onClick={() => handleUpdateStatus(b.bookingReservationID, 2)}
                          >
                            <i className="bi bi-check-lg"></i>
                          </button>
                          <button
                            className="btn btn-outline-danger btn-sm"
                            title="Hủy đơn"
                            onClick={() => handleUpdateStatus(b.bookingReservationID, 0)}
                          >
                            <i className="bi bi-x-lg"></i>
                          </button>
                        </>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal for Booking Details */}
      <Modal
        isOpen={!!selectedBooking}
        title={`Chi tiết đơn đặt phòng #${selectedBooking?.bookingReservationID}`}
        onClose={() => setSelectedBooking(null)}
      >
        {selectedBooking && (
          <div>
            <div className="bg-light p-3 rounded mb-3">
              <div className="row">
                <div className="col-6">
                  <small className="text-muted d-block">Khách hàng:</small>
                  <strong>{selectedBooking.customerFullName}</strong>
                </div>
                <div className="col-6">
                  <small className="text-muted d-block">Email:</small>
                  <strong>{selectedBooking.customerEmail}</strong>
                </div>
              </div>
            </div>

            <h6 className="fw-bold text-dark mb-2">Danh sách phòng đặt (Booking Details):</h6>
            <div className="table-responsive">
              <table className="table table-bordered table-sm align-middle">
                <thead className="table-light">
                  <tr>
                    <th>Số phòng</th>
                    <th>Loại phòng</th>
                    <th>Từ ngày</th>
                    <th>Đến ngày</th>
                    <th className="text-end">Thành tiền</th>
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
