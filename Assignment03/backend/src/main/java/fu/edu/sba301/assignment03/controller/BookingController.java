package fu.edu.sba301.assignment03.controller;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.BookingRequest;
import fu.edu.sba301.assignment03.dto.BookingResponse;
import fu.edu.sba301.assignment03.service.IBookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final IBookingService bookingService;

    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Staff role: manage booking reservation (includes booking details)
    @GetMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // Customer role: view booking reservation history
    @GetMapping("/my-history")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication auth) {
        return ResponseEntity.ok(bookingService.getMyBookings(auth.getName()));
    }

    // Customer role: create an online booking reservation with one or many rooms
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request, Authentication auth) {
        BookingResponse created = bookingService.createBooking(request, auth.getName());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Staff role: update booking reservation status (1: Confirmed, 2: Completed, 0: Cancelled)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<BookingResponse> updateBookingStatus(
        @PathVariable Integer id,
        @RequestParam Integer status
    ) {
        BookingResponse updated = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}
