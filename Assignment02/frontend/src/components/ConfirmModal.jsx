/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import React from 'react';

export const ConfirmModal = ({ isOpen, title, message, onConfirm, onCancel, confirmText = 'Xác nhận xóa', cancelText = 'Hủy' }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-backdrop-custom" onClick={onCancel}>
      <div className="modal-dialog-custom p-4" style={{ maxWidth: '450px' }} onClick={(e) => e.stopPropagation()}>
        <div className="text-center mb-3 text-danger">
          <i className="bi bi-exclamation-triangle-fill" style={{ fontSize: '3rem' }}></i>
          <h5 className="fw-bold mt-2 text-dark">{title || 'Xác nhận hành động'}</h5>
          <p className="text-muted mb-0">{message}</p>
        </div>
        <div className="d-flex justify-content-end gap-2 border-top pt-3">
          <button type="button" className="btn btn-secondary px-3" onClick={onCancel}>
            {cancelText}
          </button>
          <button type="button" className="btn btn-danger px-3" onClick={onConfirm}>
            {confirmText}
          </button>
        </div>
      </div>
    </div>
  );
};

