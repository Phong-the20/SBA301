import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useTheme } from "../context/ThemeContext";

export const LoginPage = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [fieldErrors, setFieldErrors] = useState({});
  const [showPassword, setShowPassword] = useState(false);

  const { login, isAuthenticated } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();
  const location = useLocation();

  const from = location.state?.from?.pathname || "/dashboard";

  // If already logged in, redirect
  React.useEffect(() => {
    if (isAuthenticated) {
      navigate(from, { replace: true });
    }
  }, [isAuthenticated, navigate, from]);

  const validate = () => {
    const errs = {};
    if (!username.trim()) {
      errs.username = "Vui lòng nhập tên đăng nhập!";
    }
    if (!password) {
      errs.password = "Vui lòng nhập mật khẩu!";
    }
    setFieldErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");

    if (!validate()) {
      return;
    }

    const result = login(username, password);
    if (result.success) {
      navigate(from, { replace: true });
    } else {
      setError(result.message || "Tên đăng nhập hoặc mật khẩu không chính xác!");
    }
  };

  // Quick fill helper for presentation/evaluation
  const fillCredentials = (u, p) => {
    setUsername(u);
    setPassword(p);
    setError("");
    setFieldErrors({});
  };

  return (
    <div className="min-vh-100 d-flex flex-column justify-content-center align-items-center p-3 login-background">
      {/* Top right theme toggle */}
      <div className="position-absolute top-0 end-0 p-3">
        <button
          type="button"
          className="btn btn-outline-secondary btn-sm rounded-circle d-flex align-items-center justify-content-center shadow-sm"
          style={{ width: "38px", height: "38px" }}
          onClick={toggleTheme}
          title="Đổi giao diện sáng/tối"
        >
          <i className={`bi ${theme === "light" ? "bi-moon-stars-fill text-warning" : "bi-sun-fill text-warning"}`}></i>
        </button>
      </div>

      <div className="login-card-container w-100" style={{ maxWidth: "440px" }}>
        <div className="card border-0 shadow-lg rounded-4 overflow-hidden bg-body">
          {/* Brand Header */}
          <div className="p-4 pt-5 text-center bg-primary-subtle border-bottom">
            <div className="d-inline-block shadow rounded-4 overflow-hidden mb-3">
              <img
                src="/assets/logo.jpg"
                alt="FUNews AI Logo"
                style={{ width: "72px", height: "72px", objectFit: "cover" }}
              />
            </div>
            <h4 className="fw-bold text-gradient mb-1">FUNewsManagementSystem</h4>
            <p className="text-secondary small mb-0">
              Cổng thông tin quản trị tin tức FPT University
            </p>
          </div>

          <div className="card-body p-4 p-md-5">
            <div className="mb-4 text-center">
              <h5 className="fw-bold text-body mb-1">Đăng Nhập Quản Trị</h5>
              <p className="text-secondary small">
                Vui lòng nhập tài khoản được cấp quyền để tiếp tục
              </p>
            </div>

            {error && (
              <div className="alert alert-danger d-flex align-items-center rounded-3 p-3 mb-4 shadow-sm" role="alert">
                <i className="bi bi-exclamation-triangle-fill fs-5 me-2 flex-shrink-0"></i>
                <div className="small fw-semibold">{error}</div>
              </div>
            )}

            <form onSubmit={handleSubmit} noValidate>
              {/* Username Input */}
              <div className="mb-3">
                <label className="form-label fw-semibold small text-secondary">
                  Tên đăng nhập (Username)
                </label>
                <div className="input-group">
                  <span className="input-group-text bg-body-tertiary">
                    <i className="bi bi-person"></i>
                  </span>
                  <input
                    type="text"
                    id="usernameInput"
                    className={`form-control ${fieldErrors.username ? "is-invalid" : ""}`}
                    placeholder="Nhập username..."
                    value={username}
                    onChange={(e) => {
                      setUsername(e.target.value);
                      if (fieldErrors.username) setFieldErrors((prev) => ({ ...prev, username: "" }));
                    }}
                    autoFocus
                  />
                  {fieldErrors.username && (
                    <div className="invalid-feedback">{fieldErrors.username}</div>
                  )}
                </div>
              </div>

              {/* Password Input */}
              <div className="mb-4">
                <label className="form-label fw-semibold small text-secondary">
                  Mật khẩu
                </label>
                <div className="input-group">
                  <span className="input-group-text bg-body-tertiary">
                    <i className="bi bi-lock"></i>
                  </span>
                  <input
                    type={showPassword ? "text" : "password"}
                    id="passwordInput"
                    className={`form-control ${fieldErrors.password ? "is-invalid" : ""}`}
                    placeholder="Nhập mật khẩu..."
                    value={password}
                    onChange={(e) => {
                      setPassword(e.target.value);
                      if (fieldErrors.password) setFieldErrors((prev) => ({ ...prev, password: "" }));
                    }}
                  />
                  <button
                    type="button"
                    className="btn btn-outline-secondary"
                    onClick={() => setShowPassword(!showPassword)}
                    tabIndex="-1"
                  >
                    <i className={`bi ${showPassword ? "bi-eye-slash" : "bi-eye"}`}></i>
                  </button>
                  {fieldErrors.password && (
                    <div className="invalid-feedback">{fieldErrors.password}</div>
                  )}
                </div>
              </div>

              {/* Submit Button */}
              <button
                type="submit"
                id="btnLoginSubmit"
                className="btn btn-primary w-100 py-2 rounded-3 fw-bold shadow-sm mb-4 transition-all"
              >
                <i className="bi bi-box-arrow-in-right me-2"></i>
                Đăng Nhập Vào Hệ Thống
              </button>
            </form>

            {/* Test Credentials Quick Box (Requirement R02 & R03 Demonstration) */}
            <div className="p-3 bg-body-tertiary rounded-3 border">
              <div className="d-flex align-items-center justify-content-between mb-2">
                <span className="text-secondary small fw-bold text-uppercase" style={{ fontSize: "0.7rem" }}>
                  <i className="bi bi-key-fill text-warning me-1"></i> Tài khoản Test (Bấm để điền):
                </span>
              </div>
              <div className="d-flex flex-column gap-2">
                <button
                  type="button"
                  className="btn btn-sm btn-outline-primary d-flex align-items-center justify-content-between text-start rounded-2 py-1 px-2"
                  onClick={() => fillCredentials("Admin", "Admin")}
                >
                  <span className="small">
                    <strong>Admin</strong> (Quyền Quản trị viên)
                  </span>
                  <span className="badge bg-primary text-white">Admin / Admin</span>
                </button>

                <button
                  type="button"
                  className="btn btn-sm btn-outline-success d-flex align-items-center justify-content-between text-start rounded-2 py-1 px-2"
                  onClick={() => fillCredentials("staff", "staff123")}
                >
                  <span className="small">
                    <strong>Staff</strong> (Quyền Nhân viên)
                  </span>
                  <span className="badge bg-success text-white">staff / staff123</span>
                </button>
              </div>
            </div>
          </div>
        </div>

        <div className="text-center mt-3 text-secondary small">
          SBA301 • Integrate Single Page Application with Spring Boot • Lab Assignment 01
        </div>
      </div>
    </div>
  );
};
