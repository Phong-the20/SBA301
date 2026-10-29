/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import axiosClient from './axiosClient';

export const authApi = {
  login: (accountEmail, accountPassword) => {
    return axiosClient.post('/auth/login', { accountEmail, accountPassword });
  }
};

