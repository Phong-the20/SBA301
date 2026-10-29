/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import axiosClient from './axiosClient';

export const tagApi = {
  getAll: () => axiosClient.get('/tags'),
  getById: (id) => axiosClient.get(`/tags/${id}`),
  create: (data) => axiosClient.post('/tags', data),
  update: (id, data) => axiosClient.put(`/tags/${id}`, data),
  delete: (id) => axiosClient.delete(`/tags/${id}`)
};

