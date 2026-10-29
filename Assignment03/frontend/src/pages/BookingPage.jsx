/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { roomApi } from '../api/roomApi';
import { bookingApi } from '../api/bookingApi';
import { useAuth } from '../context/AuthContext';

export const BookingPage = () => {
  const { user } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  const [availableRooms, setAvailableRooms] = useState([]);
  const [selectedRoomIds, setSelectedRoomIds] = useState([]);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    roomApi.getPublic()
      .then((data) => {
        setAvailableRooms(data || []);
        if (location.state?.preSelectedRoomId) {
          setSelectedRoomIds([location.state.preSelectedRoomId]);
        }
      })
      .catch((err) => console.error(err));

    // Default dates: tomorrow to 2 days after
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    const dayAfter = new Date();
    dayAfter.setDate(dayAfter.getDate() + 3);

    setStartDate(tomorrow.toISOString().split('T')[0]);
    setEndDate(dayAfter.toISOString().split('T')[0]);
  }, [location.state]);

  const handleToggleRoom = (roomId) => {
    setSelectedRoomIds((prev) =>
      prev.includes(roomId) ? prev.filter((id) => id !== roomId) : [...prev, roomId]
    );
  };

  // Calculate days
  const calculateDays = () => {
    if (!startDate || !endDate) return 0;
    const start = new Date(startDate);
    const end = new Date(endDate);
    const diff = (end - start) / (1000 * 60 * 60 * 24);
    return diff > 0 ? diff : 0;
  };

  const days = calculateDays();

  // Calculate estimated total price
  const estimatedTotal = selectedRoomIds.reduce((sum, id) => {
    const r = availableRooms.find((rm) => rm.roomID === id);
    if (!r) return sum;
    return sum + Number(r.roomPricePerDay) * days;
  }, 0);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (selectedRoomIds.length === 0) {
      setError('Vui lòng chọn ít nhất 1 phòng!');
      return;
    }
    if (days <= 0) {
      setError('Ngày trả phòng phải sau ngày nhận phòng!');
      return;
    }

    setLoading(true);
    try {
      const payload = {
        items: selectedRoomIds.map((roomId) => ({
          roomId,
          startDate,
          endDate
        }))
      };

      await bookingApi.create(payload);
      navigate('/customer/my-bookings');
    } catch (err) {
      setError(err.message || 'Đặt phòng thất bại');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container py-4">
      <div className="mb-4 pb-2 border-bottom">
        <h2 className="fw-bold text-dark mb-1">
          <i className="bi bi-bookmark-plus-fill text-info me-2"></i>Đặt Phòng Khách Sạn Online
        </h2>
        <p className="text-muted small mb-0">
          Khách hàng: <strong>{user?.name}</strong> ({user?.email})
        </p>
      </div>

      {error && (
        <div className="alert alert-danger d-flex align-items-center gap-2">
          <i className="bi bi-exclamation-triangle-fill"></i>
          <div>{error}</div>
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <div className="row g-4">
          {/* Left Column: Date Range & Room Selection */}
          <div className="col-lg-8">
            <div className="card shadow-sm border-0 p-4 mb-4 bg-white">
              <h5 className="fw-bold mb-3 text-dark">
                <i className="bi bi-calendar-event me-2 text-primary"></i>1. Chọn thời gian lưu trú
              </h5>
              <div className="row g-3">
                <div className="col-md-6">
                  <label className="form-label fw-semibold">Ngày nhận phòng (Check-in)</label>
                  <input
                    type="date"
                    className="form-control"
                    required
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                  />
                </div>
                <div className="col-md-6">
                  <label className="form-label fw-semibold">Ngày trả phòng (Check-out)</label>
                  <input
                    type="date"
                    className="form-control"
                    required
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                  />
                </div>
              </div>
              <div className="mt-2 text-primary small fw-semibold">
                Thời gian lưu trú: <strong>{days} ngày đêm</strong>
              </div>
            </div>

            <div className="card shadow-sm border-0 p-4 bg-white">
              <h5 className="fw-bold mb-3 text-dark">
                <i className="bi bi-door-open-fill me-2 text-primary"></i>2. Chọn một hoặc nhiều phòng
              </h5>
              <div className="row g-3">
                {availableRooms.map((room) => {
                  const isSelected = selectedRoomIds.includes(room.roomID);
                  return (
                    <div key={room.roomID} className="col-md-6">
                      <div
                        className={`p-3 border rounded-3 cursor-pointer ${isSelected ? 'border-primary bg-primary-subtle' : 'bg-light'}`}
                        style={{ cursor: 'pointer', transition: 'all 0.2s' }}
                        onClick={() => handleToggleRoom(room.roomID)}
                      >
                        <div className="form-check d-flex justify-content-between align-items-start">
                          <div>
                            <input
                              className="form-check-input"
                              type="checkbox"
                              checked={isSelected}
                              onChange={() => {}}
                            />
                            <label className="form-check-label fw-bold ms-2 fs-6">
                              Phòng {room.roomNumber}
                            </label>
                            <span className="badge bg-secondary ms-2">{room.roomTypeName}</span>
                          </div>
                          <span className="fw-bold text-danger">
                            {Number(room.roomPricePerDay).toLocaleString('vi-VN')} đ
                          </span>
                        </div>
                        <div className="small text-muted mt-2 ps-4">
                          Sức chứa: {room.roomMaxCapacity} khách | {room.roomDetailDescription || 'Đầy đủ tiện ích'}
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>

          {/* Right Column: Order Summary */}
          <div className="col-lg-4">
            <div className="card shadow-sm border-0 p-4 bg-white sticky-top" style={{ top: '80px' }}>
              <h5 className="fw-bold mb-3 text-dark border-bottom pb-2">Tóm Tắt Đặt Phòng</h5>

              <div className="mb-3">
                <div className="text-muted small">Số phòng đã chọn:</div>
                <div className="fw-bold fs-5 text-dark">{selectedRoomIds.length} phòng</div>
              </div>

              <div className="mb-3">
                <div className="text-muted small">Thời gian thuê:</div>
                <div className="fw-semibold">{days} ngày đêm ({startDate} ➔ {endDate})</div>
              </div>

              <hr />

              <div className="d-flex justify-content-between align-items-center mb-4">
                <span className="fw-bold">Tổng thanh toán:</span>
                <span className="fs-4 fw-bold text-danger">
                  {estimatedTotal.toLocaleString('vi-VN')} đ
                </span>
              </div>

              <button
                type="submit"
                className="btn btn-warning w-100 py-3 fw-bold text-dark shadow-sm"
                disabled={loading || selectedRoomIds.length === 0 || days <= 0}
              >
                {loading ? 'Đang xử lý đặt phòng...' : 'Xác Nhận Đặt Phòng Online'}
              </button>
            </div>
          </div>
        </div>
      </form>
    </div>
  );
};
