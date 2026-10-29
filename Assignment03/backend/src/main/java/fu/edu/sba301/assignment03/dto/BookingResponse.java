package fu.edu.sba301.assignment03.dto;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingResponse {

    private Integer bookingReservationID;
    private LocalDateTime bookingDate;
    private BigDecimal totalPrice;
    private Integer customerID;
    private String customerFullName;
    private String customerEmail;
    private Integer bookingStatus;
    private List<BookingItemDetail> details = new ArrayList<>();

    public BookingResponse() {
    }

    public Integer getBookingReservationID() {
        return bookingReservationID;
    }

    public void setBookingReservationID(Integer bookingReservationID) {
        this.bookingReservationID = bookingReservationID;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Integer getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(Integer bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public List<BookingItemDetail> getDetails() {
        return details;
    }

    public void setDetails(List<BookingItemDetail> details) {
        this.details = details;
    }

    public static class BookingItemDetail {
        private Integer roomID;
        private String roomNumber;
        private String roomTypeName;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal actualPrice;

        public BookingItemDetail() {
        }

        public BookingItemDetail(Integer roomID, String roomNumber, String roomTypeName, LocalDate startDate, LocalDate endDate, BigDecimal actualPrice) {
            this.roomID = roomID;
            this.roomNumber = roomNumber;
            this.roomTypeName = roomTypeName;
            this.startDate = startDate;
            this.endDate = endDate;
            this.actualPrice = actualPrice;
        }

        public Integer getRoomID() {
            return roomID;
        }

        public void setRoomID(Integer roomID) {
            this.roomID = roomID;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public void setRoomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
        }

        public String getRoomTypeName() {
            return roomTypeName;
        }

        public void setRoomTypeName(String roomTypeName) {
            this.roomTypeName = roomTypeName;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public BigDecimal getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(BigDecimal actualPrice) {
            this.actualPrice = actualPrice;
        }
    }
}
