/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { Navbar } from './components/Navbar';
import { HomePage } from './pages/HomePage';
import { LoginPage } from './pages/LoginPage';
import { AccountManagementPage } from './pages/AccountManagementPage';
import { CategoryManagementPage } from './pages/CategoryManagementPage';
import { NewsManagementPage } from './pages/NewsManagementPage';
import { MyHistoryPage } from './pages/MyHistoryPage';
import { ProfilePage } from './pages/ProfilePage';

// Route Guards
const AdminRoute = ({ children }) => {
  const { user, isAdmin } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (!isAdmin) return <Navigate to="/" replace />;
  return children;
};

const StaffRoute = ({ children }) => {
  const { user, isStaff } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (!isStaff) return <Navigate to="/" replace />;
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

              {/* Admin route */}
              <Route
                path="/admin/accounts"
                element={
                  <AdminRoute>
                    <AccountManagementPage />
                  </AdminRoute>
                }
              />

              {/* Staff routes */}
              <Route
                path="/staff/categories"
                element={
                  <StaffRoute>
                    <CategoryManagementPage />
                  </StaffRoute>
                }
              />
              <Route
                path="/staff/news"
                element={
                  <StaffRoute>
                    <NewsManagementPage />
                  </StaffRoute>
                }
              />
              <Route
                path="/staff/history"
                element={
                  <StaffRoute>
                    <MyHistoryPage />
                  </StaffRoute>
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
              <span className="text-light fw-bold">Assignment 02 - FUNewsManagementSystem</span> | Sinh viên: <strong>Tran The Phong (CE190157)</strong> - Lớp: <strong>SE1910</strong>
            </div>
          </footer>
        </div>
      </BrowserRouter>
    </AuthProvider>
  );
};

export default App;

