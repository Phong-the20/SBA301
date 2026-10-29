package fu.edu.sba301.assignment03.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.AuthRequest;
import fu.edu.sba301.assignment03.dto.AuthResponse;
import fu.edu.sba301.assignment03.dto.RegisterRequest;
import fu.edu.sba301.assignment03.entity.Customer;
import fu.edu.sba301.assignment03.exception.BusinessException;
import fu.edu.sba301.assignment03.repository.CustomerRepository;
import fu.edu.sba301.assignment03.security.JwtService;
import fu.edu.sba301.assignment03.security.StaffProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService implements IAuthService {

    private final CustomerRepository customerRepository;
    private final StaffProperties staffProperties;
    private final JwtService jwtService;

    public AuthService(CustomerRepository customerRepository, StaffProperties staffProperties, JwtService jwtService) {
        this.customerRepository = customerRepository;
        this.staffProperties = staffProperties;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new BusinessException("Email and password cannot be empty");
        }
        String email = request.getEmail().trim().toLowerCase();
        String pass = request.getPassword().trim();

        // 1. Check Staff account loaded from application.properties (as required by PDF)
        if (staffProperties.getEmail().equalsIgnoreCase(email)) {
            if (staffProperties.getPassword().equals(pass)) {
                String token = jwtService.generateToken(
                    staffProperties.getEmail(),
                    "STAFF",
                    staffProperties.getName(),
                    0
                );
                return new AuthResponse(token, "STAFF", 0, staffProperties.getName(), staffProperties.getEmail());
            } else {
                throw new BusinessException("Invalid credentials for Staff account");
            }
        }

        // 2. Check Customer from database
        Customer customer = customerRepository.findByEmailAddress(email)
            .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!customer.getPassword().equals(pass)) {
            throw new BusinessException("Invalid email or password");
        }

        if (customer.getCustomerStatus() != null && customer.getCustomerStatus() == 0) {
            throw new BusinessException("Account has been disabled. Please contact reception.");
        }

        String token = jwtService.generateToken(
            customer.getEmailAddress(),
            "CUSTOMER",
            customer.getCustomerFullName(),
            customer.getCustomerID()
        );

        return new AuthResponse(
            token,
            "CUSTOMER",
            customer.getCustomerID(),
            customer.getCustomerFullName(),
            customer.getEmailAddress()
        );
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (request == null || request.getEmailAddress() == null || request.getPassword() == null) {
            throw new BusinessException("Registration details cannot be empty");
        }
        String email = request.getEmailAddress().trim().toLowerCase();

        if (staffProperties.getEmail().equalsIgnoreCase(email)) {
            throw new BusinessException("Cannot register using administrative staff email");
        }

        if (customerRepository.existsByEmailAddress(email)) {
            throw new BusinessException("Email '" + email + "' is already registered");
        }

        Customer customer = new Customer();
        customer.setCustomerFullName(request.getCustomerFullName().trim());
        customer.setEmailAddress(email);
        customer.setTelephone(request.getTelephone() != null ? request.getTelephone().trim() : "");
        customer.setCustomerBirthday(request.getCustomerBirthday());
        customer.setPassword(request.getPassword().trim());
        customer.setCustomerStatus(1);

        Customer saved = customerRepository.save(customer);

        String token = jwtService.generateToken(
            saved.getEmailAddress(),
            "CUSTOMER",
            saved.getCustomerFullName(),
            saved.getCustomerID()
        );

        return new AuthResponse(
            token,
            "CUSTOMER",
            saved.getCustomerID(),
            saved.getCustomerFullName(),
            saved.getEmailAddress()
        );
    }
}
