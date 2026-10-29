package fu.edu.sba301.assignment03.dto;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class BookingRequest {

    @NotEmpty(message = "At least one room must be selected for booking")
    private List<BookingItem> items;

    public BookingRequest() {
    }

    public BookingRequest(List<BookingItem> items) {
        this.items = items;
    }

    public List<BookingItem> getItems() {
        return items;
    }

    public void setItems(List<BookingItem> items) {
        this.items = items;
    }

    public static class BookingItem {
        @NotNull(message = "Room ID is required")
        private Integer roomId;

        @NotNull(message = "Start date is required")
        private LocalDate startDate;

        @NotNull(message = "End date is required")
        private LocalDate endDate;

        public BookingItem() {
        }

        public BookingItem(Integer roomId, LocalDate startDate, LocalDate endDate) {
            this.roomId = roomId;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Integer getRoomId() {
            return roomId;
        }

        public void setRoomId(Integer roomId) {
            this.roomId = roomId;
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
    }
}
