package fu.edu.sba301.assignment03;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.BookingRequest;
import fu.edu.sba301.assignment03.dto.BookingResponse;
import fu.edu.sba301.assignment03.entity.BookingReservation;
import fu.edu.sba301.assignment03.entity.Customer;
import fu.edu.sba301.assignment03.entity.RoomInformation;
import fu.edu.sba301.assignment03.entity.RoomType;
import fu.edu.sba301.assignment03.exception.BusinessException;
import fu.edu.sba301.assignment03.repository.BookingReservationRepository;
import fu.edu.sba301.assignment03.repository.CustomerRepository;
import fu.edu.sba301.assignment03.repository.RoomInformationRepository;
import fu.edu.sba301.assignment03.service.BookingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTests {

    @Mock
    private BookingReservationRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RoomInformationRepository roomRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    @DisplayName("Create booking calculates total price accurately for 2 days")
    void testCreateBookingCalculatesCorrectPrice() {
        Customer cust = new Customer(1, "Customer A", "09000", "cust@test.com", LocalDate.now(), 1, "pass");
        RoomType type = new RoomType(1, "Deluxe", "Desc", "Note");
        RoomInformation room = new RoomInformation(5, "201", "Deluxe", 2, type, 1, BigDecimal.valueOf(1000000.00));

        when(customerRepository.findByEmailAddress("cust@test.com")).thenReturn(Optional.of(cust));
        when(roomRepository.findById(5)).thenReturn(Optional.of(room));
        when(bookingRepository.save(any(BookingReservation.class))).thenAnswer(invocation -> {
            BookingReservation b = invocation.getArgument(0);
            b.setBookingReservationID(99);
            return b;
        });

        BookingRequest req = new BookingRequest(Collections.singletonList(
            new BookingRequest.BookingItem(5, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 3)) // 2 days
        ));

        BookingResponse res = bookingService.createBooking(req, "cust@test.com");
        assertNotNull(res);
        assertEquals(99, res.getBookingReservationID());
        // 2 days * 1,000,000 = 2,000,000
        assertEquals(0, BigDecimal.valueOf(2000000.00).compareTo(res.getTotalPrice()));
    }

    @Test
    @DisplayName("Create booking throws BusinessException if end date is before start date")
    void testCreateBookingInvalidDates() {
        Customer cust = new Customer(1, "Customer A", "09000", "cust@test.com", LocalDate.now(), 1, "pass");
        when(customerRepository.findByEmailAddress("cust@test.com")).thenReturn(Optional.of(cust));

        BookingRequest req = new BookingRequest(Collections.singletonList(
            new BookingRequest.BookingItem(5, LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 1))
        ));

        BusinessException ex = assertThrows(BusinessException.class, () -> bookingService.createBooking(req, "cust@test.com"));
        assertTrue(ex.getMessage().contains("End date must be strictly after start date"));
    }
}
