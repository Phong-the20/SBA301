# SBA301 - Lab 07: Building Desktop Application Using JavaFX and Hibernate

Dự án này được xây dựng chính xác 100% theo 2 tài liệu hướng dẫn:
1. **`SBA301_Lab07_Detailed_Step_by_Step_JavaFX_Hibernate_Guide.pdf`**
2. **`Lab 07_Hibernate Application Development & CRUD Operations.pdf`**

Tuân thủ nghiêm ngặt **Kiến trúc phân lớp chuẩn MVC Layer Service + Repository Pattern**:
```
JavaFX / FXML View  (LoginGUI.fxml, student-view.fxml)
       ↓  (UI events / Selection)
Controllers         (LoginController, StudentController)
       ↓  (Chỉ giao tiếp với Service, không gọi DAO hay Session)
Service Layer       (IStudentService, StudentService)  <-- 100% LOGIC NGHIỆP VỤ Ở ĐÂY
       ↓  (Chỉ giao tiếp với Repository interface)
Repository Layer    (IStudentRepository, StudentRepository)
       ↓  (Persistence Contract)
DAO Layer           (StudentDAO)  <-- LỚP DUY NHẤT BIẾT VỀ HIBERNATE SESSION/HQL
       ↓
Hibernate ORM 6.5.3 (SessionFactory, Session, Transaction)
       ↓
SQL Server          (Database: LibraryDB trên localhost:1433)
       ├── STUDENTS (id, email, password, firstName, lastName, marks)
       ├── BOOKS (id, author, isbn, title)
       └── STUDENTS_BOOKS (student_id, book_id)
```

---

## 1. Thông Tin Công Nghệ & Môi Trường

- **Java**: JDK 21
- **ORM / Persistence**: Hibernate ORM 6.5.3.Final + Jakarta Persistence 3.1
- **UI Framework**: JavaFX 21.0.2 (Controls & FXML)
- **Database**: Microsoft SQL Server (`localhost:1433`, Database: `LibraryDB`, user: `sa`, pass: `12345`)
- **Unit Testing**: JUnit 5.10.3 + In-memory H2 Database (MSSQLServer mode) cho test độc lập và tốc độ tức thì.

---

## 2. Cấu Trúc Thư Mục Dự Án

```
Lab07/
├── pom.xml
├── mvnw.cmd
├── run_lab07.bat                    # Script chạy ứng dụng giao diện JavaFX 1-click
├── run_console.bat                  # Script chạy Console Smoke Test 1-click
├── README.md
└── src/
    ├── main/
    │   ├── java/fu/se/lab07/
    │   │   ├── entity/
    │   │   │   ├── Student.java     # Entity ánh xạ bảng STUDENTS (chứa password, email unique)
    │   │   │   └── Book.java        # Entity ánh xạ bảng BOOKS (isbn unique, Many-to-Many)
    │   │   ├── util/
    │   │   │   └── HibernateUtil.java # Quản lý SessionFactory duy nhất (Singleton)
    │   │   ├── dao/
    │   │   │   └── StudentDAO.java  # Thao tác Hibernate Session, HQL, Transaction commit/rollback
    │   │   ├── repository/
    │   │   │   ├── IStudentRepository.java
    │   │   │   └── StudentRepository.java # Che giấu DAO khỏi tầng Service
    │   │   ├── service/
    │   │   │   ├── IStudentService.java
    │   │   │   └── StudentService.java    # XỬ LÝ 100% BUSINESS LOGIC (validate, unique email, login, marks 0..10)
    │   │   ├── app/
    │   │   │   └── ConsoleSmokeTest.java  # Smoke test backend độc lập trước khi mở UI
    │   │   └── ui/
    │   │       ├── AppLauncher.java       # Universal launcher (chạy trực tiếp không cần module path phức tạp)
    │   │       ├── StudentApplication.java# Composition root khởi tạo SessionFactory & load LoginGUI
    │   │       └── controller/
    │   │           ├── LoginController.java   # Điều phối màn hình Login, kiểm tra xác thực và chuyển scene
    │   │           └── StudentController.java # Quản lý giao diện Student, TableView, Create/Update/Delete/Clear
    │   └── resources/
    │       ├── hibernate.cfg.xml              # Cấu hình kết nối SQL Server LibraryDB
    │       └── fu/se/lab07/ui/
    │           ├── LoginGUI.fxml              # Giao diện Đăng nhập đẹp mắt
    │           └── student-view.fxml          # Giao diện Quản lý Sinh viên (TableView + Form + CRUD Buttons)
    └── test/
        ├── java/fu/se/lab07/
        │   └── Lab07IntegrationTests.java     # 16 integration tests tự động (T01 - T16) PASS 100%
        └── resources/
            └── hibernate-test.cfg.xml         # In-memory H2 configuration cho tests
```

---

## 3. Quy Trình Hoạt Động Của Ứng Dụng (End-to-End Flow)

1. **Khởi Động**: Chạy `run_lab07.bat` hoặc chạy class `AppLauncher.java`.
2. **Màn Hình Đăng Nhập (`LoginGUI.fxml`)**:
   - Nhập Email và Password (ví dụ tài khoản có sẵn trong DB: `lab07@fpt.edu.vn` / `123456`, hoặc `s29@fu.edu.vn` / `123456`).
   - Nếu sai mật khẩu hoặc tài khoản không tồn tại: hiển thị hộp thoại `Alert ERROR: Invalid email or password`.
   - Nếu đăng nhập đúng: hệ thống tự động chuyển sang màn hình **Manage Student**.
3. **Màn Hình Quản Lý Sinh Viên (`student-view.fxml`)**:
   - Tự động nạp toàn bộ danh sách sinh viên từ `LibraryDB` lên `TableView`.
   - **Click vào một dòng trên bảng**: Tự động điền thông tin sinh viên lên Form và lấy đúng `selectedStudentId`.
   - **Thêm mới (Add Student)**: Nhập thông tin -> bấm Add -> Service kiểm tra tính hợp lệ và trùng email -> Thêm vào DB -> Cập nhật bảng ngay lập tức.
   - **Cập nhật (Update Student)**: Chọn dòng -> sửa điểm hoặc tên -> bấm Update -> cập nhật vào DB.
   - **Xóa (Delete Student)**: Chọn dòng -> bấm Delete -> hiển thị xác nhận Confirmation -> Xóa khỏi DB.
   - **Làm mới (Clear)**: Xóa trắng form và hủy chọn trên bảng.

---

## 4. Hướng Dẫn Chạy Dự Án

### Cách 1: Chạy Ứng Dụng JavaFX Bằng File `.bat` (Khuyên dùng)
Click đúp chuột vào file: [run_lab07.bat](file:///d:/Data/Documents/HSF302/LAB-SBA301/Lab07/run_lab07.bat)
*(Hoặc gõ lệnh `.\run_lab07.bat` trong Terminal).*

### Cách 2: Chạy Bằng Maven Plugin
```powershell
cd d:\Data\Documents\HSF302\LAB-SBA301\Lab07
.\mvnw.cmd compile exec:java
# hoặc nếu dùng plugin javafx:
# .\mvnw.cmd javafx:run
```

### Cách 3: Chạy Console Smoke Test (Chạy backend không cần mở giao diện)
Click đúp file: [run_console.bat](file:///d:/Data/Documents/HSF302/LAB-SBA301/Lab07/run_console.bat)
*(Hoặc chạy trực tiếp `ConsoleSmokeTest.java` trong IntelliJ).*

### Cách 4: Chạy Kiểm Thử Tự Động (16/16 Test Cases Passed)
```powershell
cd d:\Data\Documents\HSF302\LAB-SBA301\Lab07
.\mvnw.cmd test
```

---

## 5. Kết Quả Kiểm Thử (16/16 Tests PASS 100%)

Dự án tích hợp đầy đủ bộ kiểm thử tự động `Lab07IntegrationTests`:
- `T01_sessionFactory_bootstrap`: Khởi tạo Hibernate SessionFactory thành công.
- `T02_create_student_success`: Thêm sinh viên thành công, tự sinh ID.
- `T03_create_student_duplicateEmail_throwsIllegalState`: Service chặn và ném lỗi khi trùng email.
- `T04_create_student_invalidMarks_throwsIllegalArgument`: Điểm số ngoài khoảng 0..10 bị từ chối.
- `T05_findById_existing_returnsStudent`: Tìm kiếm sinh viên theo ID.
- `T06_findById_missing_returnsEmptyOrThrows`: Xử lý ngoại lệ khi không tìm thấy ID.
- `T07_findAll_returnsAllStudents`: Lấy danh sách toàn bộ sinh viên.
- `T08_update_student_success`: Cập nhật sinh viên thành công.
- `T09_update_student_missingId_throwsIllegalArgument`: Chặn cập nhật khi thiếu ID.
- `T10_delete_student_success`: Xóa sinh viên thành công.
- `T11_delete_student_missingId_throwsIllegalArgument`: Báo lỗi khi xóa ID không tồn tại.
- `T12_login_validCredentials_returnsStudent`: Đăng nhập đúng thông tin thành công.
- `T13_login_invalidPassword_throwsIllegalArgument`: Đăng nhập sai mật khẩu bị từ chối.
- `T14_login_nonExistingEmail_throwsIllegalArgument`: Đăng nhập email không tồn tại bị từ chối.
- `T15_controller_architecture_doesNotImportHibernateOrDAO`: Audit kiến trúc đảm bảo Controller không gọi Hibernate/DAO.
- `T16_service_layer_handlesAllBusinessLogic`: Tầng Service xử lý toàn bộ logic nghiệp vụ và validation.
