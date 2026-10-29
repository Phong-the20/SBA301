/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import axiosClient from './axiosClient';

export const roomApi = {
  getRooms: (all = false, keyword = '') => axiosClient.get('/rooms', { params: { all, keyword } }),
  getPublic: () => axiosClient.get('/rooms/public'),
  getById: (id) => axiosClient.get(`/rooms/${id}`),
  create: (data) => axiosClient.post('/rooms', data),
  update: (id, data) => axiosClient.put(`/rooms/${id}`, data),
  delete: (id) => axiosClient.delete(`/rooms/${id}`),
  getRoomTypes: () => axiosClient.get('/room-types')
};
