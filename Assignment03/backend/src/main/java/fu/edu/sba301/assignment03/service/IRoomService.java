package fu.edu.sba301.assignment03.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.RoomDto;
import fu.edu.sba301.assignment03.dto.RoomTypeDto;
import java.util.List;

public interface IRoomService {
    List<RoomDto> getAllRooms();
    List<RoomDto> getActiveRooms();
    RoomDto getRoomById(Integer id);
    RoomDto createRoom(RoomDto dto);
    RoomDto updateRoom(Integer id, RoomDto dto);
    void deleteRoom(Integer id);
    List<RoomDto> searchRooms(String keyword);
    List<RoomTypeDto> getAllRoomTypes();
}
