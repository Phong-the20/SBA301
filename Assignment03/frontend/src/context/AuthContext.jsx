/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React, { createContext, useContext, useState } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem('A3_TOKEN'));
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('A3_USER');
    return saved ? JSON.parse(saved) : null;
  });

  const login = (authData) => {
    setToken(authData.token);
    setUser(authData);
    localStorage.setItem('A3_TOKEN', authData.token);
    localStorage.setItem('A3_USER', JSON.stringify(authData));
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('A3_TOKEN');
    localStorage.removeItem('A3_USER');
  };

  const isStaff = user?.role === 'STAFF';
  const isCustomer = user?.role === 'CUSTOMER';

  return (
    <AuthContext.Provider value={{ token, user, login, logout, isStaff, isCustomer }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
