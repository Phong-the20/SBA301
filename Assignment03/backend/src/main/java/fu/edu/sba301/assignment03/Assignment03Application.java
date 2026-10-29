package fu.edu.sba301.assignment03;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.entity.*;
import fu.edu.sba301.assignment03.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@SpringBootApplication
public class Assignment03Application {

    public static void main(String[] args) {
        SpringApplication.run(Assignment03Application.class, args);
    }

    @Bean
    public CommandLineRunner seedDatabase(
        RoomTypeRepository roomTypeRepository,
        RoomInformationRepository roomRepository,
        CustomerRepository customerRepository,
        BookingReservationRepository bookingRepository,
        BookingDetailRepository detailRepository
    ) {
        return args -> {
            if (roomTypeRepository.count() == 0) {
                // 1. Seed Room Types
                RoomType std = roomTypeRepository.save(new RoomType(null, "Standard Room", "Phòng tiêu chuẩn 1 giường đôi", "Phù hợp cho 1-2 khách"));
                RoomType dlx = roomTypeRepository.save(new RoomType(null, "Deluxe Room", "Phòng cao cấp ban công hướng biển", "Phù hợp cho gia đình nhỏ"));
                RoomType ste = roomTypeRepository.save(new RoomType(null, "Suite VIP", "Căn hộ tổng thống sang trọng", "Dịch vụ phòng 24/7"));

                // 2. Seed Rooms
                RoomInformation r101 = roomRepository.save(new RoomInformation(null, "101", "Phòng Standard tầng 1", 2, std, 1, BigDecimal.valueOf(500000.00)));
                RoomInformation r102 = roomRepository.save(new RoomInformation(null, "102", "Phòng Standard cạnh hồ bơi", 2, std, 1, BigDecimal.valueOf(550000.00)));
                RoomInformation r201 = roomRepository.save(new RoomInformation(null, "201", "Phòng Deluxe tầng 2", 3, dlx, 1, BigDecimal.valueOf(950000.00)));
                RoomInformation r301 = roomRepository.save(new RoomInformation(null, "301", "Phòng Suite VIP tầng thượng", 4, ste, 1, BigDecimal.valueOf(2200000.00)));
                RoomInformation r401 = roomRepository.save(new RoomInformation(null, "401", "Phòng bảo trì sửa chữa", 2, std, 0, BigDecimal.valueOf(400000.00)));

                // 3. Seed Customers
                Customer c1 = customerRepository.save(new Customer(null, "Nguyen Van An", "0901234567", "an.nguyen@gmail.com", LocalDate.of(2000, 5, 15), 1, "123456"));
                Customer c2 = customerRepository.save(new Customer(null, "Le Thi Binh", "0912345678", "binh.le@gmail.com", LocalDate.of(1998, 11, 20), 1, "123456"));
                Customer c3 = customerRepository.save(new Customer(null, "Tran The Phong (Customer)", "0987654321", "phong.customer@gmail.com", LocalDate.of(2001, 8, 10), 1, "123456"));

                // 4. Seed Booking Reservation & Detail
                BookingReservation b1 = new BookingReservation();
                b1.setCustomer(c1);
                b1.setBookingDate(LocalDateTime.now().minusDays(1));
                b1.setTotalPrice(BigDecimal.valueOf(1000000.00));
                b1.setBookingStatus(1);
                b1 = bookingRepository.save(b1);

                BookingDetail d1 = new BookingDetail(
                    b1,
                    r101,
                    LocalDate.now(),
                    LocalDate.now().plusDays(2),
                    BigDecimal.valueOf(1000000.00)
                );
                detailRepository.save(d1);

                System.out.println(">>> [Assignment 03] MiniHotel Database successfully initialized! <<<");
            }
        };
    }
}
