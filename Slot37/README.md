# SBA301 - Slot 37: Spring MVC with JSP, JSTL, HomeController & MongoDB

## 1. Tổng quan đề tài (Chapter 21 Part B)
- **Công nghệ:** Spring Boot 3.3.4 (WAR Packaging), Spring MVC, Embedded Tomcat Jasper, Jakarta JSTL, Spring Form Tags, Spring Data MongoDB.
- **Mục tiêu:**
  - Cấu hình ứng dụng Spring Boot hỗ trợ render JSP/JSTL (`/WEB-INF/jsp/*.jsp`).
  - `HomeController`: Trang chủ Dashboard (`/`) tổng hợp thống kê số lượng sản phẩm (`productCount`, `activeCount`).
  - `ProductController`: Quản lý danh mục CRUD sản phẩm qua giao diện JSP:
    + Render danh sách sản phẩm với `<c:forEach>` và empty state `<c:choose>`.
    + Tái sử dụng layout dùng chung qua JSP fragments: `<jsp:include page="/WEB-INF/jsp/fragments/header.jsp" />` và `footer.jsp`.
    + Binding form bằng Spring form tags: `<form:form>`, `<form:input>`, `<form:select>`, `<form:errors>`.
    + Giữ nguyên dữ liệu dropdown category khi xảy ra lỗi validation (`@ModelAttribute("categories")`).
    + Áp dụng Post-Redirect-Get (PRG) và Flash Messages.
  - **Mô hình kiến trúc 3 lớp chuẩn mực:**
    `JSP / JSTL View` <-> `HomeController / ProductController` <-> `IProductService / ProductService` (Toàn bộ logic nghiệp vụ, validation kiểm tra tại Service) <-> `ProductRepository` <-> `MongoDB (products)`.

## 2. Cấu trúc thư mục
```
Slot37/
├── src/main/java/fu/edu/sba301/slot37/
│   ├── controller/
│   │   ├── HomeController.java            # Trang chủ Dashboard tổng quan
│   │   └── ProductController.java         # Quản lý CRUD sản phẩm qua JSP
│   ├── model/Product.java                 # Entity MongoDB có Jakarta Constraints
│   ├── repository/ProductRepository.java  # MongoRepository data access
│   ├── service/                           # IProductService, ProductService
│   └── Slot37Application.java             # Entry point kế thừa SpringBootServletInitializer
├── src/main/resources/
│   └── application.properties             # Cấu hình JSP view resolver prefix/suffix & mongodb.uri
├── src/main/webapp/WEB-INF/jsp/
│   ├── fragments/                         # header.jsp & footer.jsp
│   ├── home.jsp                           # Trang chủ Dashboard
│   └── products/
│       ├── list.jsp                       # Danh sách sản phẩm & tìm kiếm
│       └── form.jsp                       # Form thêm mới / cập nhật sản phẩm
├── src/test/java/fu/edu/sba301/slot37/
│   └── Slot37ControllerMvcTests.java      # Bộ kiểm thử MockMvc 6/6 test cases
├── pom.xml
├── run_slot37.bat                         # File 1-click khởi chạy ứng dụng
└── README.md
```

## 3. Cách chạy dự án
- **Cách 1 (1-Click):** Chạy file `run_slot37.bat`.
- **Cách 2 (Command Line):**
  ```powershell
  cd D:\Data\Documents\HSF302\LAB-SBA301\Slot37
  .\mvnw.cmd spring-boot:run
  ```
  Sau đó mở trình duyệt tại: `http://localhost:8080/`.
- **Cách 3 (Kiểm thử tự động):**
  ```powershell
  .\mvnw.cmd test
  ```
