package fu.edu.sba301.assignment03.repository;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.entity.BookingReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingReservationRepository extends JpaRepository<BookingReservation, Integer> {
    List<BookingReservation> findByCustomer_CustomerIDOrderByBookingDateDesc(Integer customerId);
    List<BookingReservation> findAllByOrderByBookingDateDesc();
    boolean existsByCustomer_CustomerID(Integer customerId);
}
