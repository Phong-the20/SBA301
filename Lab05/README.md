# SBA301 Lab 05 — Create Single Page Application with Spring Boot RESTful API

Dự án hoàn chỉnh Full-Stack **Orchid Management** theo đúng đặc tả và yêu cầu trong tài liệu **`SBA301_Lab05_Step_by_Step_FullStack_SPA_Guide.pdf`**, với cấu trúc thư mục phân tách rõ ràng thành `backend` và `frontend`.

---

## 1. Cấu Trúc Thư Mục Chuẩn

```text
SBA301-Lab05/ (hoặc Lab05/)
├── backend/                      # Spring Boot Backend RESTful API (chapter15api)
│   ├── pom.xml                   # Java 21, Spring Boot 3.3.4, JPA, MSSQL, H2
│   └── src/main/
│       ├── java/fu/se/chapter15api/
│       │   ├── Chapter15apiApplication.java
│       │   ├── configs/
│       │   │   └── CorsConfig.java          # Cấu hình CORS cho http://localhost:5173
│       │   ├── controllers/
│       │   │   └── OrchidController.java    # REST Controller (GET, POST, PUT, DELETE)
│       │   ├── pojos/
│       │   │   └── Orchid.java              # JPA Entity ánh xạ bảng Orchid
│       │   ├── repositories/
│       │   │   └── IOrchidRepository.java   # Pure Spring Data JPA (Không chứa business logic)
│       │   └── services/                    # ★ SERVICE LAYER: XỬ LÝ TOÀN BỘ LOGIC
│       │       ├── IOrchidService.java      # Service Interface
│       │       └── OrchidService.java       # Triển khai validation, dirty checking
│       └── resources/
│           └── application.properties       # Kết nối SBA301_OrchidDB (SQL Server)
│
├── frontend/                     # ReactJS Single Page Application (chapter15reactjs)
│   ├── package.json              # React 19, Vite, Axios, React Router, Bootstrap
│   ├── vite.config.js            # Port 5173
│   ├── .env                      # VITE_API_URL=http://localhost:8080/orchids
│   └── src/
│       ├── components/
│       │   ├── NavBar.jsx        # Navigation bar
│       │   ├── ListOfOrchids.jsx # Bảng danh sách, modal xem ảnh, nút Sửa/Xóa
│       │   └── EditOrchid.jsx    # Form thêm mới / cập nhật hoa lan
│       ├── services/
│       │   └── orchidApi.js      # Axios client kết nối REST API Backend
│       ├── App.jsx               # React Router config
│       └── main.jsx
│
├── test-runner.ps1               # Script tự động kiểm thử 10 Test Cases (T01 - T10)
└── README.md                     # Hướng dẫn chi tiết
```

---

## 2. Kiến Trúc MVC 3 Lớp & Quy Tắc Xử Lý Tại Service Layer

Dự án tuân thủ nghiêm ngặt nguyên lý phân tách trách nhiệm (Separation of Concerns):
- **`IOrchidRepository`**: Thuần túy là giao diện Spring Data JPA kế thừa `JpaRepository<Orchid, Integer>`, **tuyệt đối không chứa business logic hay validation**.
- **`OrchidService`**: Đảm nhiệm **100% logic nghiệp vụ**:
  1. Kiểm tra validation bắt buộc `orchidName` không được để trống hoặc chỉ có khoảng trắng (`IllegalArgumentException` -> trả về `400 Bad Request`).
  2. Kiểm tra tồn tại bản ghi bằng `findById()` trước khi Update hoặc Delete (ném ngoại lệ -> trả về `404 Not Found`).
  3. Cập nhật thuộc tính theo dirty checking và lưu lại qua repository.
  4. Quản trị transaction thông qua annotation `@Transactional`.
- **`OrchidController`**: Đóng vai trò tiếp nhận HTTP Request, gọi tầng Service và ánh xạ HTTP Response Status Codes: `200 OK`, `201 Created`, `204 No Content`, `400 Bad Request`, `404 Not Found`.

---

## 3. Hướng Dẫn Vận Hành

### Bước 1: Khởi động Backend
```bash
cd backend
java -jar target/chapter15api-1.0.0.jar
# hoặc nếu dùng maven wrapper:
# .\mvnw.cmd spring-boot:run
```
Backend sẽ lắng nghe tại: `http://localhost:8080/orchids`

### Bước 2: Khởi động Frontend
Mở một terminal mới:
```bash
cd frontend
npm run dev
```
Truy cập giao diện tại: `http://localhost:5173`

### Bước 3: Chạy Kiểm Thử Tự Động (Integration Test Suite)
Mở PowerShell tại thư mục gốc `SBA301-Lab05`:
```powershell
.\test-runner.ps1
```

Tất cả 10 Test Cases sẽ được kiểm tra và xuất bảng tổng kết kết quả (`PASS` 10/10).
