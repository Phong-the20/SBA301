/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { roomApi } from '../api/roomApi';
import { useAuth } from '../context/AuthContext';

export const HomePage = () => {
  const [rooms, setRooms] = useState([]);
  const [roomTypes, setRoomTypes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState('');
  const [selectedType, setSelectedType] = useState(null);

  const { isCustomer } = useAuth();
  const navigate = useNavigate();

  const fetchData = async () => {
    try {
      setLoading(true);
      const [roomsData, typesData] = await Promise.all([
        roomApi.getRooms(false, keyword),
        roomApi.getRoomTypes()
      ]);
      setRooms(roomsData || []);
      setRoomTypes(typesData || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [keyword]);

  const filteredRooms = selectedType
    ? rooms.filter((r) => r.roomTypeID === selectedType)
    : rooms;

  const handleBookNow = (room) => {
    navigate('/customer/book', { state: { preSelectedRoomId: room.roomID } });
  };

  return (
    <div className="container py-4">
      {/* Hero Banner */}
      <div className="p-4 p-md-5 mb-4 rounded-4 text-white shadow" style={{ background: 'linear-gradient(135deg, #0d3b66 0%, #1f2421 100%)' }}>
        <div className="col-md-9 px-0">
          <span className="badge bg-warning text-dark px-3 py-2 fw-bold mb-2">FUMiniHotelSystem - Assignment 03</span>
          <h1 className="display-5 fw-bold">Hệ Thống Đặt Phòng Khách Sạn FU Mini Hotel</h1>
          <p className="lead my-3 text-light-50">
            Trải nghiệm dịch vụ lưu trú đẳng cấp, tiện nghi và hiện đại. Quý khách có thể xem phòng trực tiếp không cần đăng nhập hoặc tạo tài khoản để đặt phòng trực tuyến tức thì.
          </p>
        </div>
      </div>

      {/* Filter and Search Bar */}
      <div className="row g-3 mb-4 align-items-center">
        <div className="col-md-6">
          <div className="input-group">
            <span className="input-group-text bg-white"><i className="bi bi-search"></i></span>
            <input
              type="text"
              className="form-control"
              placeholder="Tìm kiếm số phòng hoặc mô tả phòng..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
            />
            {keyword && (
              <button className="btn btn-outline-secondary" onClick={() => setKeyword('')}>
                Xóa
              </button>
            )}
          </div>
        </div>

        <div className="col-md-6">
          <div className="d-flex flex-wrap gap-2 justify-content-md-end">
            <button
              className={`btn btn-sm ${selectedType === null ? 'btn-primary' : 'btn-outline-primary'}`}
              onClick={() => setSelectedType(null)}
            >
              Tất cả loại phòng
            </button>
            {roomTypes.map((t) => (
              <button
                key={t.roomTypeID}
                className={`btn btn-sm ${selectedType === t.roomTypeID ? 'btn-primary' : 'btn-outline-primary'}`}
                onClick={() => setSelectedType(t.roomTypeID)}
              >
                {t.roomTypeName}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Room Listing */}
      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status"></div>
          <p className="mt-2 text-muted">Đang tải danh sách phòng khả dụng...</p>
        </div>
      ) : filteredRooms.length === 0 ? (
        <div className="text-center py-5 bg-white rounded-3 shadow-sm">
          <i className="bi bi-door-closed text-muted" style={{ fontSize: '3rem' }}></i>
          <h5 className="mt-3 text-muted">Hiện không có phòng nào phù hợp điều kiện tìm kiếm</h5>
        </div>
      ) : (
        <div className="row g-4">
          {filteredRooms.map((room) => (
            <div key={room.roomID} className="col-md-6 col-lg-4">
              <div className="card h-100 shadow-sm border-0 card-hotel">
                <div className="card-body d-flex flex-column">
                  <div className="d-flex justify-content-between align-items-center mb-2">
                    <span className="badge bg-primary px-2 py-1">
                      {room.roomTypeName || 'Tiêu chuẩn'}
                    </span>
                    <span className="badge bg-success-subtle text-success border border-success-subtle">
                      <i className="bi bi-check-circle me-1"></i>Sẵn sàng đón khách
                    </span>
                  </div>

                  <h4 className="card-title fw-bold text-dark mt-1">
                    Phòng {room.roomNumber}
                  </h4>
                  <p className="card-text text-muted flex-grow-1 small">
                    {room.roomDetailDescription || 'Phòng nghỉ đầy đủ tiện nghi, giường êm ái, wifi tốc độ cao.'}
                  </p>

                  <div className="d-flex justify-content-between align-items-center py-2 border-top border-bottom my-3">
                    <div className="small text-secondary">
                      <i className="bi bi-people-fill me-1 text-primary"></i>
                      Sức chứa: <strong>{room.roomMaxCapacity} khách</strong>
                    </div>
                    <div className="text-end">
                      <span className="fs-5 fw-bold text-danger">
                        {Number(room.roomPricePerDay).toLocaleString('vi-VN')} đ
                      </span>
                      <small className="text-muted d-block" style={{ fontSize: '0.75rem' }}>/ ngày đêm</small>
                    </div>
                  </div>

                  <button
                    className="btn btn-warning w-100 text-dark fw-bold shadow-sm"
                    onClick={() => handleBookNow(room)}
                  >
                    <i className="bi bi-calendar-plus me-1"></i>Đặt Phòng Ngay
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
