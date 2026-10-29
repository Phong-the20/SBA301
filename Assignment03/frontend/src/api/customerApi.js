/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import axiosClient from './axiosClient';

export const customerApi = {
  getAll: (keyword = '') => axiosClient.get('/customers', { params: { keyword } }),
  getById: (id) => axiosClient.get(`/customers/${id}`),
  create: (data) => axiosClient.post('/customers', data),
  update: (id, data) => axiosClient.put(`/customers/${id}`, data),
  delete: (id) => axiosClient.delete(`/customers/${id}`),
  getProfile: () => axiosClient.get('/customers/profile'),
  updateProfile: (data) => axiosClient.put('/customers/profile', data)
};
