package fu.edu.sba301.assignment03;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.CustomerDto;
import fu.edu.sba301.assignment03.entity.Customer;
import fu.edu.sba301.assignment03.exception.BusinessException;
import fu.edu.sba301.assignment03.repository.BookingReservationRepository;
import fu.edu.sba301.assignment03.repository.CustomerRepository;
import fu.edu.sba301.assignment03.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookingReservationRepository bookingRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("Create customer throws BusinessException if email is already registered")
    void testCreateCustomerDuplicateEmail() {
        CustomerDto dto = new CustomerDto(null, "Test User", "0900000000", "test@hotel.com", LocalDate.of(1995, 1, 1), 1, "pass");
        when(customerRepository.existsByEmailAddress("test@hotel.com")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> customerService.createCustomer(dto));
        assertTrue(ex.getMessage().contains("already registered"));
        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Delete customer with bookings soft deletes customer by changing status to 0")
    void testDeleteCustomerWithBookings() {
        Customer cust = new Customer(1, "Test Customer", "0911111111", "cust@hotel.com", LocalDate.now(), 1, "pass");
        when(customerRepository.findById(1)).thenReturn(Optional.of(cust));
        when(bookingRepository.existsByCustomer_CustomerID(1)).thenReturn(true);

        customerService.deleteCustomer(1);
        assertEquals(0, cust.getCustomerStatus());
        verify(customerRepository, times(1)).save(cust);
        verify(customerRepository, never()).delete(any());
    }
}
