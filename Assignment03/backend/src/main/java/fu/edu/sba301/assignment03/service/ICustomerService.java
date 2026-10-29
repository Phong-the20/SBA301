package fu.edu.sba301.assignment03.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.CustomerDto;
import java.util.List;

public interface ICustomerService {
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(Integer id);
    CustomerDto createCustomer(CustomerDto dto);
    CustomerDto updateCustomer(Integer id, CustomerDto dto);
    void deleteCustomer(Integer id);
    List<CustomerDto> searchCustomers(String keyword);
    CustomerDto getProfile(String email);
    CustomerDto updateProfile(String email, CustomerDto dto);
}
