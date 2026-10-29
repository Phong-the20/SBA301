/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import axiosClient from './axiosClient';

export const bookingApi = {
  getAll: () => axiosClient.get('/bookings'),
  getMyHistory: () => axiosClient.get('/bookings/my-history'),
  create: (data) => axiosClient.post('/bookings', data),
  updateStatus: (id, status) => axiosClient.put(`/bookings/${id}/status`, null, { params: { status } })
};
