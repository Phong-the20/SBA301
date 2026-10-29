@echo off
:: Author: Tran The Phong
:: Student ID: CE190157
:: Assignment: 03 - FUMiniHotelSystem

echo ======================================================================
echo   ASSIGNMENT 03 - FUMINIHOTEL SYSTEM (SPRING SECURITY + JWT)
echo   Sinh vien: Tran The Phong - MSSV: CE190157 - Lop: SE1910
echo ======================================================================

echo.
echo [1/2] Dang khoi dong Backend Spring Boot 3 + JWT Security (Port 8080)...
start "Assignment03 - Backend (Spring Boot + JWT)" cmd /k "cd backend && mvnw.cmd spring-boot:run"

echo.
echo [2/2] Dang khoi dong Frontend React Client (Port 3000)...
start "Assignment03 - Frontend (ReactJS)" cmd /k "cd frontend && npm install && npm run dev"

echo.
echo He thong MiniHotel da khoi dong xong!
echo Backend REST API (Public Rooms): http://localhost:8080/api/rooms
echo Frontend React Client          : http://localhost:3000
echo Tai khoan Staff (properties)   : admin@hotel.com / admin123
echo Tai khoan Customer             : an.nguyen@gmail.com / 123456
echo ======================================================================
pause
