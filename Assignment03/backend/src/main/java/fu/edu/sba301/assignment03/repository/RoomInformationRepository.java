package fu.edu.sba301.assignment03.repository;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.entity.RoomInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomInformationRepository extends JpaRepository<RoomInformation, Integer> {
    Optional<RoomInformation> findByRoomNumber(String roomNumber);
    boolean existsByRoomNumber(String roomNumber);
    List<RoomInformation> findByRoomStatus(Integer roomStatus);
    List<RoomInformation> findByRoomNumberContainingIgnoreCaseOrRoomDetailDescriptionContainingIgnoreCase(String roomNumber, String desc);
}
