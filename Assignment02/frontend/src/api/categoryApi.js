/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import axiosClient from './axiosClient';

export const categoryApi = {
  getAll: (keyword) => axiosClient.get('/categories', { params: { keyword } }),
  getActive: () => axiosClient.get('/categories/active'),
  getById: (id) => axiosClient.get(`/categories/${id}`),
  create: (data) => axiosClient.post('/categories', data),
  update: (id, data) => axiosClient.put(`/categories/${id}`, data),
  delete: (id) => axiosClient.delete(`/categories/${id}`)
};

