/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import axiosClient from './axiosClient';

export const newsApi = {
  getPublic: (keyword) => axiosClient.get('/news/public', { params: { keyword } }),
  getAll: (keyword) => axiosClient.get('/news', { params: { all: true, keyword } }),
  getMyHistory: (staffId) => axiosClient.get('/news/my-history', { params: { staffId } }),
  getById: (id) => axiosClient.get(`/news/${id}`),
  create: (data, currentUserId) => axiosClient.post('/news', data, { params: { currentUserId } }),
  update: (id, data) => axiosClient.put(`/news/${id}`, data),
  delete: (id) => axiosClient.delete(`/news/${id}`)
};

