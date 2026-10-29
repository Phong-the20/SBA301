-- Author: Tran The Phong
-- Student ID: CE190157
-- Assignment: 02 - FUNewsManagementSystem

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'A2TranThePhong_SE1910')
BEGIN
    CREATE DATABASE A2TranThePhong_SE1910;
END
GO

USE A2TranThePhong_SE1910;
GO

IF OBJECT_ID('NewsTag', 'U') IS NOT NULL DROP TABLE NewsTag;
IF OBJECT_ID('NewsArticle', 'U') IS NOT NULL DROP TABLE NewsArticle;
IF OBJECT_ID('Tag', 'U') IS NOT NULL DROP TABLE Tag;
IF OBJECT_ID('Category', 'U') IS NOT NULL DROP TABLE Category;
IF OBJECT_ID('SystemAccount', 'U') IS NOT NULL DROP TABLE SystemAccount;
GO

CREATE TABLE SystemAccount (
    accountId INT IDENTITY(1,1) PRIMARY KEY,
    accountName NVARCHAR(100) NOT NULL,
    accountEmail VARCHAR(100) NOT NULL UNIQUE,
    accountRole INT NOT NULL, -- 1: Admin, 2: Staff
    accountPassword VARCHAR(100) NOT NULL
);
GO

CREATE TABLE Category (
    categoryId INT IDENTITY(1,1) PRIMARY KEY,
    categoryName NVARCHAR(100) NOT NULL,
    categoryDesciption NVARCHAR(250),
    status INT NOT NULL DEFAULT 1 -- 1: Active, 0: Inactive
);
GO

CREATE TABLE Tag (
    tagId INT IDENTITY(1,1) PRIMARY KEY,
    tagName NVARCHAR(100) NOT NULL,
    note NVARCHAR(250)
);
GO

CREATE TABLE NewsArticle (
    newsArticleId INT IDENTITY(1,1) PRIMARY KEY,
    newsTitle NVARCHAR(200) NOT NULL,
    headline NVARCHAR(250),
    createdDate DATETIME DEFAULT GETDATE(),
    newsContent NVARCHAR(MAX),
    newsSource NVARCHAR(100),
    categoryId INT NOT NULL FOREIGN KEY REFERENCES Category(categoryId),
    newsStatus INT NOT NULL DEFAULT 1, -- 1: Active, 0: Inactive
    createdBy INT NOT NULL FOREIGN KEY REFERENCES SystemAccount(accountId),
    modifiedDate DATETIME
);
GO

CREATE TABLE NewsTag (
    newsArticleId INT NOT NULL FOREIGN KEY REFERENCES NewsArticle(newsArticleId) ON DELETE CASCADE,
    tagId INT NOT NULL FOREIGN KEY REFERENCES Tag(tagId) ON DELETE CASCADE,
    PRIMARY KEY (newsArticleId, tagId)
);
GO

-- Sample Data
INSERT INTO SystemAccount (accountName, accountEmail, accountRole, accountPassword) VALUES
('Super Admin', 'admin@news.com', 1, 'admin123'),
('Tran The Phong (Staff)', 'phongtt@news.com', 2, 'staff123'),
('Staff Demo', 'staff@news.com', 2, 'staff123');

INSERT INTO Category (categoryName, categoryDesciption, status) VALUES
(N'Công nghệ', N'Tin tức công nghệ trong và ngoài nước', 1),
(N'Giáo dục', N'Các hoạt động học tập, đào tạo tại Đại học FPT', 1),
(N'Đời sống', N'Hoạt động ngoại khóa và văn hóa sinh viên', 1),
(N'Lưu trữ', N'Danh mục cũ tạm ngưng hoạt động', 0);

INSERT INTO Tag (tagName, note) VALUES
(N'AI & Robot', N'Chủ đề trí tuệ nhân tạo'),
(N'FPT University', N'Hoạt động nhà trường'),
(N'Sinh viên', N'Góc học tập & giải trí');

INSERT INTO NewsArticle (newsTitle, headline, createdDate, newsContent, newsSource, categoryId, newsStatus, createdBy, modifiedDate) VALUES
(N'Đại học FPT đẩy mạnh ứng dụng AI trong giảng dạy', N'Xu hướng đào tạo công nghệ 2026', GETDATE(), N'Nội dung chi tiết về các workshop và phòng lab AI mới...', N'FPT News', 1, 1, 2, GETDATE()),
(N'Hội thảo khoa học công nghệ quốc tế năm 2026', N'Gặp gỡ các chuyên gia đầu ngành', GETDATE(), N'Hội thảo quy tụ hơn 50 diễn giả quốc tế...', N'Tech Daily', 1, 1, 2, GETDATE()),
(N'Sinh viên khóa mới tự tin bước vào môi trường đại học', N'Lễ khai giảng rực rỡ', GETDATE(), N'Hàng ngàn tân sinh viên tham gia ngày hội định hướng...', N'Campus Life', 2, 1, 3, GETDATE());

INSERT INTO NewsTag (newsArticleId, tagId) VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 2),
(3, 3);
GO

