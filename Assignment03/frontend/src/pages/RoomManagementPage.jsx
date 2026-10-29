/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React, { useEffect, useState } from 'react';
import { roomApi } from '../api/roomApi';
import { Modal } from '../components/Modal';
import { ConfirmModal } from '../components/ConfirmModal';

export const RoomManagementPage = () => {
  const [rooms, setRooms] = useState([]);
  const [roomTypes, setRoomTypes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [errorAlert, setErrorAlert] = useState('');
  const [successAlert, setSuccessAlert] = useState('');

  // Modal State for Create / Edit
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    roomID: null,
    roomNumber: '',
    roomDetailDescription: '',
    roomMaxCapacity: 2,
    roomTypeID: '',
    roomStatus: 1,
    roomPricePerDay: 500000
  });

  // Modal State for Delete Confirmation
  const [deleteId, setDeleteId] = useState(null);
  const [roomToDelete, setRoomToDelete] = useState(null);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [roomsData, typesData] = await Promise.all([
        roomApi.getRooms(true, keyword),
        roomApi.getRoomTypes()
      ]);
      setRooms(roomsData || []);
      setRoomTypes(typesData || []);
    } catch (err) {
      setErrorAlert(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [keyword]);

  const handleOpenCreate = () => {
    setIsEditing(false);
    setFormData({
      roomID: null,
      roomNumber: '',
      roomDetailDescription: '',
      roomMaxCapacity: 2,
      roomTypeID: roomTypes.length > 0 ? roomTypes[0].roomTypeID : '',
      roomStatus: 1,
      roomPricePerDay: 500000
    });
    setErrorAlert('');
    setIsModalOpen(true);
  };

  const handleOpenEdit = (room) => {
    setIsEditing(true);
    setFormData({
      roomID: room.roomID,
      roomNumber: room.roomNumber,
      roomDetailDescription: room.roomDetailDescription || '',
      roomMaxCapacity: room.roomMaxCapacity,
      roomTypeID: room.roomTypeID,
      roomStatus: room.roomStatus,
      roomPricePerDay: room.roomPricePerDay
    });
    setErrorAlert('');
    setIsModalOpen(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setErrorAlert('');
    setSuccessAlert('');
    try {
      if (isEditing) {
        await roomApi.update(formData.roomID, formData);
        setSuccessAlert('Cập nhật thông tin phòng thành công!');
      } else {
        await roomApi.create(formData);
        setSuccessAlert('Thêm phòng mới thành công!');
      }
      setIsModalOpen(false);
      fetchData();
    } catch (err) {
      setErrorAlert(err.message);
    }
  };

  const handleOpenDelete = (room) => {
    setDeleteId(room.roomID);
    setRoomToDelete(room);
    setErrorAlert('');
  };

  const handleConfirmDelete = async () => {
    if (!deleteId) return;
    setErrorAlert('');
    setSuccessAlert('');
    try {
      await roomApi.delete(deleteId);
      setSuccessAlert('Xử lý xóa phòng thành công (nếu phòng đã từng có giao dịch thuê, hệ thống tự động đổi trạng thái sang Inactive theo quy định đề bài)!');
      setDeleteId(null);
      setRoomToDelete(null);
      fetchData();
    } catch (err) {
      setErrorAlert(err.message);
      setDeleteId(null);
      setRoomToDelete(null);
    }
  };

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
          <h2 className="fw-bold text-dark mb-1">
            <i className="bi bi-grid-3x3-gap-fill text-warning me-2"></i>Quản Lý Phòng Khách Sạn (Staff)
          </h2>
          <p className="text-muted small mb-0">
            Quản lý thông tin phòng, giá thuê và trạng thái bảo trì/hoạt động
          </p>
        </div>
        <button className="btn btn-warning fw-bold shadow-sm" onClick={handleOpenCreate}>
          <i className="bi bi-plus-circle-fill me-1"></i>Thêm phòng mới
        </button>
      </div>

      {/* Notifications */}
      {errorAlert && (
        <div className="alert alert-danger alert-dismissible fade show d-flex align-items-center gap-2" role="alert">
          <i className="bi bi-exclamation-triangle-fill fs-5"></i>
          <div>{errorAlert}</div>
          <button type="button" className="btn-close" onClick={() => setErrorAlert('')}></button>
        </div>
      )}

      {successAlert && (
        <div className="alert alert-success alert-dismissible fade show d-flex align-items-center gap-2" role="alert">
          <i className="bi bi-check-circle-fill fs-5"></i>
          <div>{successAlert}</div>
          <button type="button" className="btn-close" onClick={() => setSuccessAlert('')}></button>
        </div>
      )}

      {/* Search Input */}
      <div className="card shadow-sm border-0 mb-4 p-3 bg-white">
        <div className="input-group">
          <span className="input-group-text bg-light"><i className="bi bi-search"></i></span>
          <input
            type="text"
            className="form-control"
            placeholder="Tìm kiếm phòng theo số phòng hoặc mô tả..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
        </div>
      </div>

      {/* Table */}
      <div className="card shadow-sm border-0">
        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0">
            <thead className="table-light">
              <tr>
                <th style={{ width: '80px' }}>ID</th>
                <th>Số phòng</th>
                <th>Loại phòng</th>
                <th>Sức chứa</th>
                <th>Giá / ngày đêm</th>
                <th>Trạng thái</th>
                <th className="text-end" style={{ width: '160px' }}>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="7" className="text-center py-4 text-muted">
                    <div className="spinner-border spinner-border-sm me-2"></div> Đang tải danh sách phòng...
                  </td>
                </tr>
              ) : rooms.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-4 text-muted">Không tìm thấy phòng nào.</td>
                </tr>
              ) : (
                rooms.map((room) => (
                  <tr key={room.roomID}>
                    <td className="fw-bold">#{room.roomID}</td>
                    <td className="fw-bold text-dark fs-6">Phòng {room.roomNumber}</td>
                    <td>
                      <span className="badge bg-primary-subtle text-primary border border-primary-subtle">
                        {room.roomTypeName}
                      </span>
                    </td>
                    <td>{room.roomMaxCapacity} người</td>
                    <td className="fw-bold text-danger">
                      {Number(room.roomPricePerDay).toLocaleString('vi-VN')} đ
                    </td>
                    <td>
                      <span className={`badge ${room.roomStatus === 1 ? 'bg-success' : 'bg-secondary'}`}>
                        {room.roomStatus === 1 ? 'Active (1)' : 'Inactive / Deleted (0)'}
                      </span>
                    </td>
                    <td className="text-end">
                      <button
                        className="btn btn-outline-secondary btn-sm me-2"
                        title="Chỉnh sửa"
                        onClick={() => handleOpenEdit(room)}
                      >
                        <i className="bi bi-pencil-square"></i>
                      </button>
                      <button
                        className="btn btn-outline-danger btn-sm"
                        title="Xóa phòng"
                        onClick={() => handleOpenDelete(room)}
                      >
                        <i className="bi bi-trash-fill"></i>
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal for Create/Edit */}
      <Modal
        isOpen={isModalOpen}
        title={isEditing ? 'Chỉnh sửa phòng' : 'Thêm phòng mới'}
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleSave}>
          <div className="row g-3 mb-3">
            <div className="col-md-6">
              <label className="form-label fw-semibold">Số phòng <span className="text-danger">*</span></label>
              <input
                type="text"
                className="form-control"
                required
                placeholder="VD: 101, 202..."
                value={formData.roomNumber}
                onChange={(e) => setFormData({ ...formData, roomNumber: e.target.value })}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label fw-semibold">Loại phòng <span className="text-danger">*</span></label>
              <select
                className="form-select"
                required
                value={formData.roomTypeID}
                onChange={(e) => setFormData({ ...formData, roomTypeID: Number(e.target.value) })}
              >
                <option value="">-- Chọn loại phòng --</option>
                {roomTypes.map((t) => (
                  <option key={t.roomTypeID} value={t.roomTypeID}>
                    {t.roomTypeName}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="row g-3 mb-3">
            <div className="col-md-6">
              <label className="form-label fw-semibold">Sức chứa tối đa <span className="text-danger">*</span></label>
              <input
                type="number"
                min="1"
                className="form-control"
                required
                value={formData.roomMaxCapacity}
                onChange={(e) => setFormData({ ...formData, roomMaxCapacity: Number(e.target.value) })}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label fw-semibold">Giá thuê / ngày (VNĐ) <span className="text-danger">*</span></label>
              <input
                type="number"
                min="1000"
                step="1000"
                className="form-control"
                required
                value={formData.roomPricePerDay}
                onChange={(e) => setFormData({ ...formData, roomPricePerDay: Number(e.target.value) })}
              />
            </div>
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Mô tả phòng</label>
            <textarea
              className="form-control"
              rows="3"
              placeholder="Tiện ích phòng, tầm nhìn, trang thiết bị..."
              value={formData.roomDetailDescription}
              onChange={(e) => setFormData({ ...formData, roomDetailDescription: e.target.value })}
            ></textarea>
          </div>

          <div className="mb-4">
            <label className="form-label fw-semibold">Trạng thái phòng <span className="text-danger">*</span></label>
            <select
              className="form-select"
              value={formData.roomStatus}
              onChange={(e) => setFormData({ ...formData, roomStatus: Number(e.target.value) })}
            >
              <option value={1}>Active (Hoạt động bình thường - 1)</option>
              <option value={0}>Inactive / Bảo trì (0)</option>
            </select>
          </div>

          <div className="d-flex justify-content-end gap-2 border-top pt-3">
            <button type="button" className="btn btn-secondary" onClick={() => setIsModalOpen(false)}>
              Hủy
            </button>
            <button type="submit" className="btn btn-warning fw-bold px-4">
              {isEditing ? 'Lưu thay đổi' : 'Tạo mới'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <ConfirmModal
        isOpen={!!deleteId}
        title="Xác nhận xóa phòng"
        message={`Bạn có chắc muốn xóa phòng "${roomToDelete?.roomNumber}" không? Theo quy định: Nếu phòng này đã từng có giao dịch thuê (booking detail), hệ thống sẽ tự động chuyển trạng thái sang Inactive (0) để bảo toàn dữ liệu lịch sử.`}
        onConfirm={handleConfirmDelete}
        onCancel={() => { setDeleteId(null); setRoomToDelete(null); }}
      />
    </div>
  );
};
