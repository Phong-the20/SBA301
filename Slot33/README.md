# SBA301 - Slot 33: Spring Data MongoDB CRUD, Export & Java Driver

## 1. Tổng quan đề tài (Chapter 19 Part B)
- **Công nghệ:** Spring Boot 3.3.4, Spring Data MongoDB, MongoDB Java Driver Sync, Jackson Databind.
- **Mục tiêu:**
  - Ánh xạ Domain `@Document("products")` sang collection MongoDB.
  - Sử dụng `MongoRepository` để thực hiện các thao tác CRUD và derived queries (`findByCategoryAndActiveTrue`, `findByNameContainingIgnoreCase`).
  - Phân trang với `Pageable` & sắp xếp `Sort`.
  - Sử dụng `MongoTemplate` để thực hiện truy vấn `Query` + `Criteria` và cập nhật từng phần nguyên tử (Atomic Partial Update) với `Update.inc("stock", amount)`.
  - Xuất dữ liệu collection sang định dạng **JSON Lines (`.jsonl`)** qua cursor stream.
  - **Mô hình kiến trúc chuẩn 3 lớp:**
    `CommandLineRunner / Controller` -> `IProductService / ProductService` (Toàn bộ Business Logic, validation giá > 0, stock >= 0 tại Service) -> `ProductRepository` & `MongoTemplate`.

## 2. Cấu trúc thư mục
```
Slot33/
├── src/main/java/fu/edu/sba301/slot33/
│   ├── model/Product.java                # Entity MongoDB ánh xạ collection products
│   ├── repository/ProductRepository.java # MongoRepository CRUD & derived query methods
│   ├── service/                          # IProductService, ProductService, IExportService, ExportService
│   └── Slot33Application.java            # Spring Boot entry point & Demo CommandLineRunner
├── src/main/resources/
│   └── application.properties            # Cấu hình mongodb.uri (default localhost:27017/slot33db)
├── src/test/java/fu/edu/sba301/slot33/
│   └── ProductServiceTests.java          # Bộ kiểm thử Unit & Integration Tests cho Service
├── pom.xml
├── run_slot33.bat                        # File 1-click khởi chạy dự án
└── README.md
```

## 3. Cách chạy dự án
- **Cách 1 (1-Click):** Chạy file `run_slot33.bat`.
- **Cách 2 (Command Line):**
  ```powershell
  cd D:\Data\Documents\HSF302\LAB-SBA301\Slot33
  .\mvnw.cmd spring-boot:run
  ```
- **Cách 3 (Kiểm thử tự động):**
  ```powershell
  .\mvnw.cmd test
  ```
