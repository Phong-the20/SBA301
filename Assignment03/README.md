# Assignment 03 - FUMiniHotelSystem (Securing REST Services with Spring Boot & JWT)

> **Tác giả:** Tran The Phong  
> **MSSV:** CE190157  
> **Lớp:** SE1910  
> **Môn:** SBA301 - Integrate Single Page Application with Spring Boot  

---

## 1. Giới thiệu bài toán & Mục tiêu
Hệ thống **FUMiniHotelSystem** là hệ thống quản lý và đặt phòng khách sạn trực tuyến, được bảo mật toàn diện bằng **Spring Security + JSON Web Token (JWT)**:
- **Backend**: Spring Boot 3 + Spring Data JPA + Spring Security + JWT + MS SQL Server (Database: `A3TranThePhong_SE1910`).
- **Frontend**: ReactJS (Vite) + Axios (tự động gắn `Authorization: Bearer <token>`) + Bootstrap 5.
- **Toàn bộ Business Logic và Validation được xử lý nghiêm ngặt tại Tầng Service (`*Service.java`)**.

---

## 2. Các quy tắc nghiệp vụ quan trọng (Xử lý tại Tầng Service)
1. **Toàn vẹn khi xóa Phòng (`RoomService.deleteRoom`)**:
   - Nếu phòng **đã từng có giao dịch thuê (booking detail)**: Hệ thống **chuyển trạng thái `roomStatus = 0` (Soft delete)** để bảo toàn lịch sử giao dịch kế toán, không xóa vật lý khỏi database.
   - Nếu phòng **chưa từng phát sinh giao dịch thuê**: Hệ thống thực hiện xóa vật lý (`roomRepository.delete()`).
2. **Cấu hình tài khoản Staff từ `application.properties`**:
   - Theo đúng yêu cầu PDF: *"The Staff account will get from application.properties file."*
   - Tài khoản Staff được định nghĩa trong `application.properties`:
     ```properties
     hotel.staff.email=admin@hotel.com
     hotel.staff.password=admin123
     hotel.staff.name=Tran The Phong (Staff Admin)
     hotel.staff.role=STAFF
     ```
   - Khi đăng nhập với email này, hệ thống cấp JWT Role `STAFF`.
3. **Phân quyền và Bảo mật**:
   - **Public (Không cần xác thực)**: Xem danh sách phòng và chi tiết phòng khả dụng (`GET /api/rooms`, `GET /api/room-types`).
   - **Customer (Khách hàng)**: Đăng ký tài khoản, Đặt phòng online với 1 hoặc nhiều phòng (`POST /api/bookings`), Quản lý hồ sơ cá nhân, Xem lịch sử đặt phòng của mình (`GET /api/bookings/my-history`).
   - **Staff (Quản trị viên khách sạn)**: Quản lý khách hàng, Quản lý phòng, Quản lý đơn đặt phòng và cập nhật trạng thái đơn (1: Confirmed, 2: Completed, 0: Cancelled).

---

## 3. Tài khoản kiểm thử nhanh (Demo Accounts)
- **Staff (Cấu hình trong `application.properties`)**:
  - Email: `admin@hotel.com`
  - Mật khẩu: `admin123`
  - Quyền hạn: Quản lý phòng, Khách hàng, Đơn đặt phòng.
- **Customer (Khách hàng trong Database)**:
  - Email: `an.nguyen@gmail.com`
  - Mật khẩu: `123456`
  - Quyền hạn: Đặt phòng trực tuyến, Xem lịch sử đặt phòng.

---

## 4. Hướng dẫn chạy chương trình
### Cách 1: Chạy 1 chạm
Nhấp đúp chuột vào file:
```cmd
run_assignment03.bat
```

### Cách 2: Chạy thủ công
- **Khởi động Backend**:
  ```powershell
  cd backend
  .\mvnw.cmd spring-boot:run
  ```
  Backend chạy tại: `http://localhost:8080`

- **Kiểm thử tự động (Unit / Integration Tests)**:
  ```powershell
  cd backend
  .\mvnw.cmd test
  ```
  *(100% Passed: 7/7 test cases verify security & business rules)*

- **Khởi động Frontend**:
  ```powershell
  cd frontend
  npm install
  npm run dev
  ```
  Frontend chạy tại: `http://localhost:3000`
