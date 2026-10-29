/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import axiosClient from './axiosClient';

export const accountApi = {
  getAll: (keyword) => axiosClient.get('/accounts', { params: { keyword } }),
  getById: (id) => axiosClient.get(`/accounts/${id}`),
  create: (data) => axiosClient.post('/accounts', data),
  update: (id, data) => axiosClient.put(`/accounts/${id}`, data),
  delete: (id) => axiosClient.delete(`/accounts/${id}`),
  updateProfile: (id, data) => axiosClient.put(`/accounts/profile/${id}`, data)
};
