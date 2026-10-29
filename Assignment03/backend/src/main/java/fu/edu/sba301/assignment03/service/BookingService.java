package fu.edu.sba301.assignment03.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.BookingRequest;
import fu.edu.sba301.assignment03.dto.BookingResponse;
import fu.edu.sba301.assignment03.entity.BookingDetail;
import fu.edu.sba301.assignment03.entity.BookingReservation;
import fu.edu.sba301.assignment03.entity.Customer;
import fu.edu.sba301.assignment03.entity.RoomInformation;
import fu.edu.sba301.assignment03.exception.BusinessException;
import fu.edu.sba301.assignment03.exception.ResourceNotFoundException;
import fu.edu.sba301.assignment03.repository.BookingReservationRepository;
import fu.edu.sba301.assignment03.repository.CustomerRepository;
import fu.edu.sba301.assignment03.repository.RoomInformationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService implements IBookingService {

    private final BookingReservationRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomInformationRepository roomRepository;

    public BookingService(
        BookingReservationRepository bookingRepository,
        CustomerRepository customerRepository,
        RoomInformationRepository roomRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAllByOrderByBookingDateDesc().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String customerEmail) {
        Customer customer = customerRepository.findByEmailAddress(customerEmail.trim().toLowerCase())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + customerEmail));
        return bookingRepository.findByCustomer_CustomerIDOrderByBookingDateDesc(customer.getCustomerID()).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BookingResponse createBooking(BookingRequest request, String customerEmail) {
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException("Booking must contain at least one room selection.");
        }

        Customer customer = customerRepository.findByEmailAddress(customerEmail.trim().toLowerCase())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + customerEmail));

        BookingReservation reservation = new BookingReservation();
        reservation.setCustomer(customer);
        reservation.setBookingDate(LocalDateTime.now());
        reservation.setBookingStatus(1); // 1: Confirmed

        BigDecimal total = BigDecimal.ZERO;
        List<BookingDetail> details = new ArrayList<>();

        for (BookingRequest.BookingItem item : request.getItems()) {
            if (item.getStartDate() == null || item.getEndDate() == null) {
                throw new BusinessException("Start date and end date must not be empty.");
            }
            if (!item.getEndDate().isAfter(item.getStartDate())) {
                throw new BusinessException("End date must be strictly after start date.");
            }

            RoomInformation room = roomRepository.findById(item.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + item.getRoomId()));

            if (room.getRoomStatus() == null || room.getRoomStatus() == 0) {
                throw new BusinessException("Room " + room.getRoomNumber() + " is currently inactive or under maintenance.");
            }

            long days = ChronoUnit.DAYS.between(item.getStartDate(), item.getEndDate());
            if (days <= 0) days = 1;

            BigDecimal itemCost = room.getRoomPricePerDay().multiply(BigDecimal.valueOf(days));
            total = total.add(itemCost);

            BookingDetail detail = new BookingDetail(
                reservation,
                room,
                item.getStartDate(),
                item.getEndDate(),
                itemCost
            );
            details.add(detail);
        }

        reservation.setTotalPrice(total);
        reservation.setBookingDetails(details);

        BookingReservation saved = bookingRepository.save(reservation);
        return toResponse(saved);
    }

    @Override
    public BookingResponse updateBookingStatus(Integer bookingId, Integer newStatus) {
        BookingReservation existing = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking reservation not found with ID: " + bookingId));

        if (newStatus == null || (newStatus != 0 && newStatus != 1 && newStatus != 2)) {
            throw new BusinessException("Status must be 1 (Confirmed), 2 (Completed), or 0 (Cancelled).");
        }

        existing.setBookingStatus(newStatus);
        BookingReservation saved = bookingRepository.save(existing);
        return toResponse(saved);
    }

    private BookingResponse toResponse(BookingReservation entity) {
        BookingResponse res = new BookingResponse();
        res.setBookingReservationID(entity.getBookingReservationID());
        res.setBookingDate(entity.getBookingDate());
        res.setTotalPrice(entity.getTotalPrice());
        if (entity.getCustomer() != null) {
            res.setCustomerID(entity.getCustomer().getCustomerID());
            res.setCustomerFullName(entity.getCustomer().getCustomerFullName());
            res.setCustomerEmail(entity.getCustomer().getEmailAddress());
        }
        res.setBookingStatus(entity.getBookingStatus());

        if (entity.getBookingDetails() != null) {
            res.setDetails(entity.getBookingDetails().stream().map(d -> {
                RoomInformation r = d.getRoom();
                return new BookingResponse.BookingItemDetail(
                    r != null ? r.getRoomID() : null,
                    r != null ? r.getRoomNumber() : "",
                    (r != null && r.getRoomType() != null) ? r.getRoomType().getRoomTypeName() : "",
                    d.getStartDate(),
                    d.getEndDate(),
                    d.getActualPrice()
                );
            }).collect(Collectors.toList()));
        }
        return res;
    }
}
