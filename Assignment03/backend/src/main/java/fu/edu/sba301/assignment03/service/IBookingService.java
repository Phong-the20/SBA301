package fu.edu.sba301.assignment03.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.BookingRequest;
import fu.edu.sba301.assignment03.dto.BookingResponse;
import java.util.List;

public interface IBookingService {
    List<BookingResponse> getAllBookings();
    List<BookingResponse> getMyBookings(String customerEmail);
    BookingResponse createBooking(BookingRequest request, String customerEmail);
    BookingResponse updateBookingStatus(Integer bookingId, Integer newStatus);
}
