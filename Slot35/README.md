# SBA301 - Slot 35: MongoRepository Query Methods, JSON @Query & MongoTemplate

## 1. Tổng quan đề tài (Chapter 20)
- **Công nghệ:** Spring Boot 3.3.4, Spring Data MongoDB.
- **Mục tiêu:**
  - Nắm vững các phương thức truy vấn `MongoRepository`:
    + Derived query: `findByCategory`, `findByActiveTrue`, `findByPriceBetween`, `findByNameContainingIgnoreCase`, `findByCategoryAndActiveTrue`, `findByActiveTrueOrderByPriceDesc`.
    + Giới hạn kết quả: `findTop3ByActiveTrueOrderByPriceDesc`.
    + Truy vấn tùy biến bằng JSON: `@Query("{ 'active': true, 'stock': { $gte: ?0 } }")`.
    + Giới hạn trường trả về (Projection): `@Query(value="{ 'active': true }", fields="{ 'name': 1, 'price': 1 }")`.
  - Triển khai **Dynamic Criteria Builder** bằng `MongoTemplate` trong lớp Service:
    + Ghép nối linh hoạt các điều kiện tìm kiếm động: `category`, `keyword`, `minPrice`, `maxPrice`, `active`, `Sort`, `Pageable`.
    + Cập nhật từng phần nguyên tử (Partial update): `Update.set("stock", newStock)`.
  - **Mô hình kiến trúc chuẩn 3 lớp:**
    `Runner / Controller` -> `IProductService / ProductService` & `IProductQueryService / ProductQueryService` (Xử lý toàn bộ logic nghiệp vụ) -> `ProductRepository` & `MongoTemplate`.

## 2. Cấu trúc thư mục
```
Slot35/
├── src/main/java/fu/edu/sba301/slot35/
│   ├── model/Product.java                # Entity MongoDB ánh xạ collection products
│   ├── repository/ProductRepository.java # MongoRepository chứa toàn bộ derived & @Query methods
│   ├── service/                          # IProductService, ProductService, IProductQueryService, ProductQueryService
│   └── Slot35Application.java            # Spring Boot entry point & demo CommandLineRunner
├── src/main/resources/
│   └── application.properties            # Cấu hình mongodb.uri
├── src/test/java/fu/edu/sba301/slot35/
│   └── Slot35ServiceAndQueryTests.java   # Bộ kiểm thử Unit & Integration Tests cho Service
├── pom.xml
├── run_slot35.bat                        # File 1-click khởi chạy dự án
└── README.md
```

## 3. Cách chạy dự án
- **Cách 1 (1-Click):** Chạy file `run_slot35.bat`.
- **Cách 2 (Command Line):**
  ```powershell
  cd D:\Data\Documents\HSF302\LAB-SBA301\Slot35
  .\mvnw.cmd spring-boot:run
  ```
- **Cách 3 (Kiểm thử tự động):**
  ```powershell
  .\mvnw.cmd test
  ```
