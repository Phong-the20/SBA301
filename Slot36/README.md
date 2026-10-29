# SBA301 - Slot 36: Spring MVC Thymeleaf, MongoDB Backend & PRG Pattern

## 1. Tổng quan đề tài (Chapter 21 Part A)
- **Công nghệ:** Spring Boot 3.3.4, Spring MVC, Thymeleaf, Spring Data MongoDB, Jakarta Validation, Bootstrap 5.3.
- **Mục tiêu:**
  - Xây dựng ứng dụng Server-side Rendering MVC hoàn chỉnh kết nối cơ sở dữ liệu MongoDB.
  - Sử dụng **PRG Pattern (Post-Redirect-Get)** kết hợp `RedirectAttributes` flash message để ngăn chặn double-submit khi refresh trang.
  - Xử lý Form Validation với `@Valid`, `@NotBlank`, `@PositiveOrZero`, và hiển thị thông báo lỗi trực quan bằng `th:errors`.
  - Quản lý tập trung ngoại lệ toàn cục bằng `@ControllerAdvice` (`GlobalMvcExceptionHandler`) xử lý `ProductNotFoundException`.
  - **Mô hình kiến trúc 3 lớp (3-Tier Layered Architecture):**
    `View (Thymeleaf)` <-> `ProductController` <-> `IProductService / ProductService` (Toàn bộ logic nghiệp vụ, validation nằm tại Service) <-> `ProductRepository` <-> `MongoDB (products)`.

## 2. Cấu trúc thư mục
```
Slot36/
├── src/main/java/fu/edu/sba301/slot36/
│   ├── controller/
│   │   ├── ProductController.java         # MVC Controller (List, Search, Create, Edit, Delete)
│   │   └── GlobalMvcExceptionHandler.java # @ControllerAdvice xử lý lỗi tập trung
│   ├── exception/
│   │   └── ProductNotFoundException.java  # Custom runtime exception
│   ├── model/Product.java                 # Entity MongoDB có Jakarta Constraints
│   ├── repository/ProductRepository.java  # MongoRepository data access
│   ├── service/                           # IProductService, ProductService
│   └── Slot36Application.java             # Entry point & CommandLineRunner
├── src/main/resources/
│   ├── application.properties             # Cấu hình server port 8080 & mongodb.uri
│   └── templates/
│       ├── products/list.html             # Giao diện danh sách sản phẩm & tìm kiếm
│       ├── products/form.html             # Giao diện thêm mới & chỉnh sửa có validation
│       └── error/not-found.html           # Trang báo lỗi 404 thân thiện
├── src/test/java/fu/edu/sba301/slot36/
│   └── ProductControllerMvcTests.java     # Bộ kiểm thử tự động MockMvc 7/7 test cases
├── pom.xml
├── run_slot36.bat                         # File 1-click khởi chạy ứng dụng
└── README.md
```

## 3. Cách chạy dự án
- **Cách 1 (1-Click):** Chạy file `run_slot36.bat`.
- **Cách 2 (Command Line):**
  ```powershell
  cd D:\Data\Documents\HSF302\LAB-SBA301\Slot36
  .\mvnw.cmd spring-boot:run
  ```
  Sau đó mở trình duyệt tại: `http://localhost:8080/products`.
- **Cách 3 (Kiểm thử tự động):**
  ```powershell
  .\mvnw.cmd test
  ```
