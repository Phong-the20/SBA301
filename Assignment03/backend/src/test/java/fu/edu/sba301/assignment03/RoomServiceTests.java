package fu.edu.sba301.assignment03;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.RoomDto;
import fu.edu.sba301.assignment03.entity.RoomInformation;
import fu.edu.sba301.assignment03.entity.RoomType;
import fu.edu.sba301.assignment03.exception.BusinessException;
import fu.edu.sba301.assignment03.repository.BookingDetailRepository;
import fu.edu.sba301.assignment03.repository.RoomInformationRepository;
import fu.edu.sba301.assignment03.repository.RoomTypeRepository;
import fu.edu.sba301.assignment03.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTests {

    @Mock
    private RoomInformationRepository roomRepository;

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @Mock
    private BookingDetailRepository bookingDetailRepository;

    @InjectMocks
    private RoomService roomService;

    private RoomInformation sampleRoom;
    private RoomType sampleType;

    @BeforeEach
    void setUp() {
        sampleType = new RoomType(1, "Standard", "Desc", "Note");
        sampleRoom = new RoomInformation(10, "101", "Standard Room", 2, sampleType, 1, BigDecimal.valueOf(500000.00));
    }

    @Test
    @DisplayName("Create room throws BusinessException if room number already exists")
    void testCreateRoomDuplicateNumber() {
        RoomDto dto = new RoomDto(null, "101", "Desc", 2, 1, "Standard", 1, BigDecimal.valueOf(500000.00));
        when(roomRepository.existsByRoomNumber("101")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> roomService.createRoom(dto));
        assertTrue(ex.getMessage().contains("already exists"));
        verify(roomRepository, never()).save(any());
    }

    @Test
    @DisplayName("Delete room with existing renting transactions performs soft delete (changes status to 0)")
    void testDeleteRoomSoftDeleteWhenBooked() {
        when(roomRepository.findById(10)).thenReturn(Optional.of(sampleRoom));
        // Critical requirement: Room stored in booking detail -> just change the status
        when(bookingDetailRepository.existsByRoom_RoomID(10)).thenReturn(true);

        roomService.deleteRoom(10);

        assertEquals(0, sampleRoom.getRoomStatus());
        verify(roomRepository, times(1)).save(sampleRoom);
        verify(roomRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Delete room without renting transactions performs hard delete")
    void testDeleteRoomHardDeleteWhenNotBooked() {
        when(roomRepository.findById(10)).thenReturn(Optional.of(sampleRoom));
        when(bookingDetailRepository.existsByRoom_RoomID(10)).thenReturn(false);

        roomService.deleteRoom(10);

        verify(roomRepository, times(1)).delete(sampleRoom);
        verify(roomRepository, never()).save(any());
    }
}
