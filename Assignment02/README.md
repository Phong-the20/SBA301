# Assignment 02 - FUNewsManagementSystem

> **Tác giả:** Tran The Phong  
> **MSSV:** CE190157  
> **Lớp:** SE1910  
> **Môn:** SBA301 - Integrate Single Page Application with Spring Boot  

---

## 1. Giới thiệu bài toán & Mục tiêu
Hệ thống **FUNewsManagementSystem** là hệ thống quản lý tin tức trường đại học, xây dựng theo kiến trúc **3-tier Layered Architecture**:
- **Backend**: Spring Boot 3 + Spring Data JPA + MS SQL Server (Database: `A2TranThePhong_SE1910`)
- **Frontend**: ReactJS (Vite) + Axios + Bootstrap 5
- **Toàn bộ Business Logic và Validation nằm trọn vẹn tại Tầng Service (`*Service.java`)**.

---

## 2. Các quy tắc nghiệp vụ quan trọng (Xử lý tại Tầng Service)
1. **Toàn vẹn khi xóa Tài khoản (`AccountService.deleteAccount`)**:
   - Nếu tài khoản đã tạo bài viết tin tức (`newsArticleRepository.existsByCreatedBy_AccountId(id) == true`) ➔ Service ném `BusinessException` ngăn chặn xóa.
2. **Toàn vẹn khi xóa Danh mục (`CategoryService.deleteCategory`)**:
   - Nếu danh mục đang được gắn với bài viết (`newsArticleRepository.existsByCategory_CategoryId(id) == true`) ➔ Service ném `BusinessException` ngăn chặn xóa.
3. **Phân quyền vai trò (Role-Based Access Control)**:
   - **Admin (Role = 1)**: Quản lý tài khoản người dùng (CRUD, Search).
   - **Staff (Role = 2)**: Quản lý danh mục, Quản lý bài viết (bao gồm tags), Quản lý hồ sơ cá nhân, Xem lịch sử đăng bài của bản thân.
   - **Public (Không cần đăng nhập)**: Xem danh sách và chi tiết các bài viết đang Active (`newsStatus = 1`).
4. **Quy chuẩn giao diện (UI Standard)**:
   - Thêm mới và Chỉnh sửa được thực hiện qua **Popup Dialog (Modal)**.
   - Xóa luôn đi kèm **Hộp thoại xác nhận (Confirm Dialog)**.

---

## 3. Tài khoản kiểm thử nhanh (Demo Accounts)
- **Admin**:
  - Email: `admin@news.com`
  - Mật khẩu: `admin123`
  - Quyền hạn: Quản lý tài khoản (CRUD & Search).
- **Staff**:
  - Email: `phongtt@news.com`
  - Mật khẩu: `staff123`
  - Quyền hạn: Quản lý danh mục, bài viết, từ khóa, hồ sơ cá nhân và lịch sử đăng bài.

---

## 4. Hướng dẫn chạy chương trình
### Cách 1: Chạy 1 chạm
Nhấp đúp chuột vào file:
```cmd
run_assignment02.bat
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
  *(100% Passed: 9/9 test cases verify business rules)*

- **Khởi động Frontend**:
  ```powershell
  cd frontend
  npm install
  npm run dev
  ```
  Frontend chạy tại: `http://localhost:3000`

