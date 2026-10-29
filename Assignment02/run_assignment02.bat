@echo off
:: Author: Tran The Phong
:: Student ID: CE190157
:: Assignment: 02 - FUNewsManagementSystem

echo ======================================================================
echo   ASSIGNMENT 02 - FUNEWSMANAGEMENT SYSTEM
echo   Sinh vien: Tran The Phong - MSSV: CE190157 - Lop: SE1910
echo ======================================================================

echo.
echo [1/2] Dang khoi dong Backend Spring Boot 3 REST API (Port 8080)...
start "Assignment02 - Backend (Spring Boot)" cmd /k "cd backend && mvnw.cmd spring-boot:run"

echo.
echo [2/2] Dang khoi dong Frontend React Client (Port 3000)...
start "Assignment02 - Frontend (ReactJS)" cmd /k "cd frontend && npm install && npm run dev"

echo.
echo He thong da khoi dong xong!
echo Backend Swagger / API : http://localhost:8080/api/news
echo Frontend React Client  : http://localhost:3000
echo Tai khoan Admin        : admin@news.com / admin123
echo Tai khoan Staff        : phongtt@news.com / staff123
echo ======================================================================
pause
