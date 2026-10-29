/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { Navbar } from './components/Navbar';
import { HomePage } from './pages/HomePage';
import { LoginPage } from './pages/LoginPage';
import { RegisterPage } from './pages/RegisterPage';
import { RoomManagementPage } from './pages/RoomManagementPage';
import { CustomerManagementPage } from './pages/CustomerManagementPage';
import { BookingManagementPage } from './pages/BookingManagementPage';
import { BookingPage } from './pages/BookingPage';
import { MyBookingsPage } from './pages/MyBookingsPage';
import { ProfilePage } from './pages/ProfilePage';

// Route Guards
const StaffRoute = ({ children }) => {
  const { user, isStaff } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (!isStaff) return <Navigate to="/" replace />;
  return children;
};

const CustomerRoute = ({ children }) => {
  const { user, isCustomer } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (!isCustomer) return <Navigate to="/" replace />;
  return children;
};

const AuthenticatedRoute = ({ children }) => {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  return children;
};

export const App = () => {
  return (
    <AuthProvider>
      <BrowserRouter>
        <div className="min-vh-100 d-flex flex-column bg-light">
          <Navbar />
          <main className="flex-grow-1">
            <Routes>
              {/* Public route */}
              <Route path="/" element={<HomePage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />

              {/* Staff routes */}
              <Route
                path="/staff/rooms"
                element={
                  <StaffRoute>
                    <RoomManagementPage />
                  </StaffRoute>
                }
              />
              <Route
                path="/staff/customers"
                element={
                  <StaffRoute>
                    <CustomerManagementPage />
                  </StaffRoute>
                }
              />
              <Route
                path="/staff/bookings"
                element={
                  <StaffRoute>
                    <BookingManagementPage />
                  </StaffRoute>
                }
              />

              {/* Customer routes */}
              <Route
                path="/customer/book"
                element={
                  <CustomerRoute>
                    <BookingPage />
                  </CustomerRoute>
                }
              />
              <Route
                path="/customer/my-bookings"
                element={
                  <CustomerRoute>
                    <MyBookingsPage />
                  </CustomerRoute>
                }
              />

              {/* Authenticated route */}
              <Route
                path="/profile"
                element={
                  <AuthenticatedRoute>
                    <ProfilePage />
                  </AuthenticatedRoute>
                }
              />

              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </main>

          <footer className="bg-dark text-white-50 text-center py-3 mt-5 border-top">
            <div className="container small">
              <span className="text-light fw-bold">Assignment 03 - FUMiniHotelSystem</span> | Sinh viên: <strong>Tran The Phong (CE190157)</strong> - Lớp: <strong>SE1910</strong>
            </div>
          </footer>
        </div>
      </BrowserRouter>
    </AuthProvider>
  );
};

export default App;
