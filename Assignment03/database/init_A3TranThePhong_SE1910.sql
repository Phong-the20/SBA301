-- Author: Tran The Phong
-- Student ID: CE190157
-- Assignment: 03 - FUMiniHotelSystem

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'A3TranThePhong_SE1910')
BEGIN
    CREATE DATABASE A3TranThePhong_SE1910;
END
GO

USE A3TranThePhong_SE1910;
GO

IF OBJECT_ID('BookingDetail', 'U') IS NOT NULL DROP TABLE BookingDetail;
IF OBJECT_ID('BookingReservation', 'U') IS NOT NULL DROP TABLE BookingReservation;
IF OBJECT_ID('Customer', 'U') IS NOT NULL DROP TABLE Customer;
IF OBJECT_ID('RoomInformation', 'U') IS NOT NULL DROP TABLE RoomInformation;
IF OBJECT_ID('RoomType', 'U') IS NOT NULL DROP TABLE RoomType;
GO

CREATE TABLE RoomType (
    roomTypeID INT IDENTITY(1,1) PRIMARY KEY,
    roomTypeName NVARCHAR(100) NOT NULL,
    typeDescription NVARCHAR(250),
    typeNote NVARCHAR(250)
);
GO

CREATE TABLE RoomInformation (
    roomID INT IDENTITY(1,1) PRIMARY KEY,
    roomNumber VARCHAR(50) NOT NULL UNIQUE,
    roomDetailDescription NVARCHAR(MAX),
    roomMaxCapacity INT NOT NULL,
    roomTypeID INT NOT NULL FOREIGN KEY REFERENCES RoomType(roomTypeID),
    roomStatus INT NOT NULL DEFAULT 1, -- 1: Active, 0: Inactive / Deleted
    roomPricePerDay DECIMAL(18,2) NOT NULL
);
GO

CREATE TABLE Customer (
    customerID INT IDENTITY(1,1) PRIMARY KEY,
    customerFullName NVARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    emailAddress VARCHAR(100) NOT NULL UNIQUE,
    customerBirthday DATE,
    customerStatus INT NOT NULL DEFAULT 1, -- 1: Active, 0: Inactive
    password VARCHAR(100) NOT NULL
);
GO

CREATE TABLE BookingReservation (
    bookingReservationID INT IDENTITY(1,1) PRIMARY KEY,
    bookingDate DATETIME DEFAULT GETDATE(),
    totalPrice DECIMAL(18,2) NOT NULL,
    customerID INT NOT NULL FOREIGN KEY REFERENCES Customer(customerID),
    bookingStatus INT NOT NULL DEFAULT 1 -- 1: Confirmed, 2: Completed, 0: Cancelled
);
GO

CREATE TABLE BookingDetail (
    bookingReservationID INT NOT NULL FOREIGN KEY REFERENCES BookingReservation(bookingReservationID) ON DELETE CASCADE,
    roomID INT NOT NULL FOREIGN KEY REFERENCES RoomInformation(roomID),
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    actualPrice DECIMAL(18,2) NOT NULL,
    PRIMARY KEY (bookingReservationID, roomID)
);
GO

-- Sample Data
INSERT INTO RoomType (roomTypeName, typeDescription, typeNote) VALUES
(N'Standard Room', N'Phòng tiêu chuẩn 1 giường đôi, tiện nghi cơ bản', N'Phù hợp cho 1-2 khách'),
(N'Deluxe Room', N'Phòng cao cấp ban công view biển, bồn tắm nằm', N'Phù hợp cho gia đình nhỏ'),
(N'Suite VIP', N'Căn hộ tổng thống sang trọng kèm phòng khách riêng', N'Dịch vụ VIP 24/7');

INSERT INTO RoomInformation (roomNumber, roomDetailDescription, roomMaxCapacity, roomTypeID, roomStatus, roomPricePerDay) VALUES
('101', N'Phòng Standard tầng 1 view sân vườn', 2, 1, 1, 500000.00),
('102', N'Phòng Standard tầng 1 cạnh hồ bơi', 2, 1, 1, 550000.00),
('201', N'Phòng Deluxe tầng 2 ban công thoáng mát', 3, 2, 1, 950000.00),
('301', N'Phòng Suite tổng thống tầng thượng', 4, 3, 1, 2200000.00),
('401', N'Phòng đang sửa chữa bảo trì định kỳ', 2, 1, 0, 400000.00);

INSERT INTO Customer (customerFullName, telephone, emailAddress, customerBirthday, customerStatus, password) VALUES
(N'Nguyen Van An', '0901234567', 'an.nguyen@gmail.com', '2000-05-15', 1, '123456'),
(N'Le Thi Binh', '0912345678', 'binh.le@gmail.com', '1998-11-20', 1, '123456'),
(N'Tran The Phong', '0987654321', 'phong.tran@gmail.com', '2001-08-10', 1, '123456');

INSERT INTO BookingReservation (bookingDate, totalPrice, customerID, bookingStatus) VALUES
(GETDATE(), 1000000.00, 1, 1),
(GETDATE(), 1900000.00, 2, 1);

INSERT INTO BookingDetail (bookingReservationID, roomID, startDate, endDate, actualPrice) VALUES
(1, 1, CAST(GETDATE() AS DATE), DATEADD(day, 2, CAST(GETDATE() AS DATE)), 500000.00),
(2, 3, CAST(GETDATE() AS DATE), DATEADD(day, 2, CAST(GETDATE() AS DATE)), 950000.00);
GO
