# SBA301 - Slot 38: DevOps, CI/CD, DevTools, Remote Debug (JDWP) & LiveReload

## 1. Tổng quan đề tài (Chapter 22)
- **Công nghệ:** Spring Boot 3.3.4, Spring Boot DevTools, Spring Boot Actuator, JDWP (Java Debug Wire Protocol), GitHub Actions CI/CD.
- **Mục tiêu:**
  - Nắm vững 2 vòng đời phản hồi kỹ thuật độc lập (Dual Engineering Lifecycle):
    1. **Local Developer Feedback Loop:**
       - Tự động restart khi thay đổi mã nguồn qua `spring-boot-devtools`.
       - Kiểm soát restart có chủ đích qua trigger file (`spring.devtools.restart.trigger-file=.reloadtrigger`).
       - Phân biệt giữa Application Restart (thay đổi Java class) và Browser LiveReload (thay đổi static view/css trên port 35729).
       - Khởi chạy JDWP Remote Debugging trên port 5005:
         + Chế độ Listen (`suspend=n`): Chạy bình thường, gắn debugger bất cứ lúc nào.
         + Chế độ Startup Suspend (`suspend=y`): Đợi gắn debugger rồi mới nạp Context.
    2. **Shared Continuous Integration & Continuous Delivery (CI/CD Pipeline):**
       - Pipeline `.github/workflows/ci.yml` tự động build, chạy test bằng `./mvnw -B clean verify`.
       - Package và lưu trữ deployable JAR artifact.
       - Quality gate: Chặn deploy nếu unit test thất bại.
       - Mô phỏng Staging Deployment & Smoke Check qua Actuator `/actuator/health`.
  - **Mô hình kiến trúc 3 lớp (3-Tier Layered Architecture):**
    `Web / REST Clients` <-> `DevOpsController` <-> `IDevOpsService / DevOpsService` (Toàn bộ logic nghiệp vụ, tính toán metrics, chẩn đoán sức khỏe tại Service) <-> `Actuator / JVM MXBeans`.

## 2. Cấu trúc thư mục
```
Slot38/
├── .github/workflows/
│   └── ci.yml                         # Kịch bản CI/CD Pipeline as Code hoàn chỉnh
├── src/main/java/fu/edu/sba301/slot38/
│   ├── controller/DevOpsController.java # REST Controller chẩn đoán hệ thống
│   ├── model/AppStatus.java           # DTO trạng thái & uptime ứng dụng
│   ├── service/                       # IDevOpsService, DevOpsService
│   └── Slot38Application.java         # Spring Boot entry point
├── src/main/resources/
│   ├── application.properties         # Cấu hình DevTools, trigger-file & Actuator
│   └── .reloadtrigger                 # Trigger file kích hoạt restart có chủ đích
├── src/test/java/fu/edu/sba301/slot38/
│   └── DevOpsServiceAndControllerTests.java # Bộ kiểm thử tự động MockMvc 3/3 tests
├── run_slot38.bat                     # File 1-click khởi chạy bình thường
├── run_remote_debug_listen.bat        # Khởi chạy JDWP port 5005 (suspend=n)
├── run_remote_debug_suspend.bat       # Khởi chạy JDWP port 5005 (suspend=y)
├── pom.xml
└── README.md
```

## 3. Cách chạy dự án
- **Cách 1: Chạy bình thường với DevTools:**
  Chạy file `run_slot38.bat` hoặc:
  ```powershell
  cd D:\Data\Documents\HSF302\LAB-SBA301\Slot38
  .\mvnw.cmd spring-boot:run
  ```
- **Cách 2: Chạy Remote Debug (JDWP port 5005):**
  Chạy file `run_remote_debug_listen.bat` rồi mở IDE attach Remote JVM Debug.
- **Cách 3: Kiểm thử tự động:**
  ```powershell
  .\mvnw.cmd test
  ```
