package fu.edu.sba301.assignment03.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.CustomerDto;
import fu.edu.sba301.assignment03.entity.Customer;
import fu.edu.sba301.assignment03.exception.BusinessException;
import fu.edu.sba301.assignment03.exception.ResourceNotFoundException;
import fu.edu.sba301.assignment03.repository.BookingReservationRepository;
import fu.edu.sba301.assignment03.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final BookingReservationRepository bookingRepository;

    public CustomerService(CustomerRepository customerRepository, BookingReservationRepository bookingRepository) {
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Integer id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        return toDto(customer);
    }

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        if (dto == null || dto.getEmailAddress() == null || dto.getEmailAddress().trim().isEmpty()) {
            throw new BusinessException("Customer email cannot be empty.");
        }
        String email = dto.getEmailAddress().trim().toLowerCase();
        if (customerRepository.existsByEmailAddress(email)) {
            throw new BusinessException("Email '" + email + "' is already registered.");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new BusinessException("Password cannot be blank.");
        }

        Customer customer = new Customer();
        customer.setCustomerFullName(dto.getCustomerFullName().trim());
        customer.setEmailAddress(email);
        customer.setTelephone(dto.getTelephone() != null ? dto.getTelephone().trim() : "");
        customer.setCustomerBirthday(dto.getCustomerBirthday());
        customer.setCustomerStatus(dto.getCustomerStatus() != null ? dto.getCustomerStatus() : 1);
        customer.setPassword(dto.getPassword().trim());

        Customer saved = customerRepository.save(customer);
        return toDto(saved);
    }

    @Override
    public CustomerDto updateCustomer(Integer id, CustomerDto dto) {
        Customer existing = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        String newEmail = dto.getEmailAddress().trim().toLowerCase();
        if (!existing.getEmailAddress().equalsIgnoreCase(newEmail) && customerRepository.existsByEmailAddress(newEmail)) {
            throw new BusinessException("Email '" + newEmail + "' is already registered by another customer.");
        }

        existing.setCustomerFullName(dto.getCustomerFullName().trim());
        existing.setEmailAddress(newEmail);
        existing.setTelephone(dto.getTelephone() != null ? dto.getTelephone().trim() : "");
        existing.setCustomerBirthday(dto.getCustomerBirthday());
        if (dto.getCustomerStatus() != null) {
            existing.setCustomerStatus(dto.getCustomerStatus());
        }
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            existing.setPassword(dto.getPassword().trim());
        }

        Customer saved = customerRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public void deleteCustomer(Integer id) {
        Customer existing = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        // If customer has booking history, disable rather than crashing foreign key
        boolean hasBookings = bookingRepository.existsByCustomer_CustomerID(id);
        if (hasBookings) {
            existing.setCustomerStatus(0);
            customerRepository.save(existing);
        } else {
            customerRepository.delete(existing);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> searchCustomers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCustomers();
        }
        String clean = keyword.trim();
        return customerRepository.findByCustomerFullNameContainingIgnoreCaseOrEmailAddressContainingIgnoreCase(clean, clean)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getProfile(String email) {
        Customer customer = customerRepository.findByEmailAddress(email.trim().toLowerCase())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
        return toDto(customer);
    }

    @Override
    public CustomerDto updateProfile(String email, CustomerDto dto) {
        Customer customer = customerRepository.findByEmailAddress(email.trim().toLowerCase())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));

        customer.setCustomerFullName(dto.getCustomerFullName().trim());
        if (dto.getTelephone() != null) {
            customer.setTelephone(dto.getTelephone().trim());
        }
        if (dto.getCustomerBirthday() != null) {
            customer.setCustomerBirthday(dto.getCustomerBirthday());
        }
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            customer.setPassword(dto.getPassword().trim());
        }

        Customer saved = customerRepository.save(customer);
        return toDto(saved);
    }

    private CustomerDto toDto(Customer entity) {
        return new CustomerDto(
            entity.getCustomerID(),
            entity.getCustomerFullName(),
            entity.getTelephone(),
            entity.getEmailAddress(),
            entity.getCustomerBirthday(),
            entity.getCustomerStatus(),
            entity.getPassword()
        );
    }
}
