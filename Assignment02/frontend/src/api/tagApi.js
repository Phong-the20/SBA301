/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import axiosClient from './axiosClient';

export const tagApi = {
  getAll: () => axiosClient.get('/tags'),
  create: (data) => axiosClient.post('/tags', data)
};

