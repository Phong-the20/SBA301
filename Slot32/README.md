# SBA301 - Slot 32: MongoDB / NoSQL / Document Model / Compass

## 1. Tổng quan đề tài (Chapter 19 Part A)
- **Cơ sở dữ liệu:** MongoDB (NoSQL Document Store).
- **Mục tiêu:**
  - Nắm vững khái niệm NoSQL Document Model, BSON representations và logical namespace: `database -> collection -> document -> fields`.
  - Thiết kế Schema Document linh hoạt (Flexible Schema) kết hợp 2 kỹ thuật cốt lõi:
    1. **Embedded Documents & Arrays:** Lưu cấu hình `specs` và kích thước `dimensions` trực tiếp trong product; mảng thẻ `tags`.
    2. **Referenced Documents:** Lưu tham chiếu `categoryId` trỏ tới collection `categories`.
  - **Mô hình kiến trúc 3 lớp (3-Tier Layered Architecture):**
    `Console Application` -> `IProductService / ProductService` & `ICategoryService / CategoryService` (toàn bộ logic, validation schema, toàn vẹn quan hệ nằm tại Service!) -> `Repository` -> `MongoDB Storage`.

## 2. Cấu trúc thư mục
```
Slot32/
├── data/
│   ├── categories.json            # Dữ liệu mẫu danh mục
│   ├── products.json              # Dữ liệu mẫu sản phẩm có embedded specs, dimensions & tags
│   ├── bad_sample_price_type.json # Mẫu dữ liệu lỗi (Type Drift) phục vụ Break-It test
│   └── compass_queries.js         # Tập lệnh truy vấn mẫu cho MongoDB Compass / mongosh
├── src/
│   ├── main/java/fu/edu/sba301/slot32/
│   │   ├── model/                 # Product, Category, ProductSpecs, Dimensions
│   │   ├── repository/            # ICategoryRepository, IProductRepository
│   │   ├── service/               # ICategoryService, CategoryService, IProductService, ProductService
│   │   └── Slot32Application.java # Chạy demo toàn bộ 20 bước kiểm thử
│   └── test/java/fu/edu/sba301/slot32/
│       └── Slot32ServiceIntegrationTests.java # 6 integration tests kiểm thử logic
├── pom.xml
├── run_slot32.bat                 # File 1-click chạy demo cho thầy xem
└── README.md
```

## 3. Cách chạy dự án
- **Cách 1 (1-Click):** Chạy file `run_slot32.bat`.
- **Cách 2 (Command Line):**
  ```powershell
  cd D:\Data\Documents\HSF302\LAB-SBA301\Slot32
  .\mvnw.cmd compile exec:java
  ```
- **Cách 3 (Chạy kiểm thử Automated Tests):**
  ```powershell
  .\mvnw.cmd test
  ```
