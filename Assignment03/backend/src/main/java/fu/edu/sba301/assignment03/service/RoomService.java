package fu.edu.sba301.assignment03.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.RoomDto;
import fu.edu.sba301.assignment03.dto.RoomTypeDto;
import fu.edu.sba301.assignment03.entity.RoomInformation;
import fu.edu.sba301.assignment03.entity.RoomType;
import fu.edu.sba301.assignment03.exception.BusinessException;
import fu.edu.sba301.assignment03.exception.ResourceNotFoundException;
import fu.edu.sba301.assignment03.repository.BookingDetailRepository;
import fu.edu.sba301.assignment03.repository.RoomInformationRepository;
import fu.edu.sba301.assignment03.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomService implements IRoomService {

    private final RoomInformationRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final BookingDetailRepository bookingDetailRepository;

    public RoomService(
        RoomInformationRepository roomRepository,
        RoomTypeRepository roomTypeRepository,
        BookingDetailRepository bookingDetailRepository
    ) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.bookingDetailRepository = bookingDetailRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getAllRooms() {
        return roomRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getActiveRooms() {
        // Requirement: View room info without authentication (active rooms)
        return roomRepository.findByRoomStatus(1).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDto getRoomById(Integer id) {
        RoomInformation room = roomRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));
        return toDto(room);
    }

    @Override
    public RoomDto createRoom(RoomDto dto) {
        if (dto == null || dto.getRoomNumber() == null || dto.getRoomNumber().trim().isEmpty()) {
            throw new BusinessException("Room number cannot be empty.");
        }
        String number = dto.getRoomNumber().trim();
        if (roomRepository.existsByRoomNumber(number)) {
            throw new BusinessException("Room number '" + number + "' already exists.");
        }
        if (dto.getRoomTypeID() == null) {
            throw new BusinessException("Room type must be selected.");
        }

        RoomType type = roomTypeRepository.findById(dto.getRoomTypeID())
            .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + dto.getRoomTypeID()));

        RoomInformation room = new RoomInformation();
        room.setRoomNumber(number);
        room.setRoomDetailDescription(dto.getRoomDetailDescription() != null ? dto.getRoomDetailDescription().trim() : "");
        room.setRoomMaxCapacity(dto.getRoomMaxCapacity() != null ? dto.getRoomMaxCapacity() : 2);
        room.setRoomType(type);
        room.setRoomStatus(dto.getRoomStatus() != null ? dto.getRoomStatus() : 1);
        room.setRoomPricePerDay(dto.getRoomPricePerDay());

        RoomInformation saved = roomRepository.save(room);
        return toDto(saved);
    }

    @Override
    public RoomDto updateRoom(Integer id, RoomDto dto) {
        RoomInformation existing = roomRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));

        String newNumber = dto.getRoomNumber().trim();
        if (!existing.getRoomNumber().equalsIgnoreCase(newNumber) && roomRepository.existsByRoomNumber(newNumber)) {
            throw new BusinessException("Room number '" + newNumber + "' already exists.");
        }

        if (dto.getRoomTypeID() != null) {
            RoomType type = roomTypeRepository.findById(dto.getRoomTypeID())
                .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + dto.getRoomTypeID()));
            existing.setRoomType(type);
        }

        existing.setRoomNumber(newNumber);
        existing.setRoomDetailDescription(dto.getRoomDetailDescription() != null ? dto.getRoomDetailDescription().trim() : "");
        if (dto.getRoomMaxCapacity() != null) {
            existing.setRoomMaxCapacity(dto.getRoomMaxCapacity());
        }
        if (dto.getRoomStatus() != null) {
            existing.setRoomStatus(dto.getRoomStatus());
        }
        if (dto.getRoomPricePerDay() != null) {
            existing.setRoomPricePerDay(dto.getRoomPricePerDay());
        }

        RoomInformation saved = roomRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public void deleteRoom(Integer id) {
        RoomInformation existing = roomRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));

        // Critical Business Requirement from PDF:
        // "The delete action will delete room information in the case this information is not belong to any renting transaction.
        // If the room information is already stored in a booking detail, just change the status."
        boolean hasRentingTransaction = bookingDetailRepository.existsByRoom_RoomID(id);
        if (hasRentingTransaction) {
            // Already has booking detail -> soft delete by changing status to 0
            existing.setRoomStatus(0);
            roomRepository.save(existing);
        } else {
            // No renting transaction -> hard delete record
            roomRepository.delete(existing);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> searchRooms(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllRooms();
        }
        String clean = keyword.trim();
        return roomRepository.findByRoomNumberContainingIgnoreCaseOrRoomDetailDescriptionContainingIgnoreCase(clean, clean)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeDto> getAllRoomTypes() {
        return roomTypeRepository.findAll().stream()
            .map(t -> new RoomTypeDto(t.getRoomTypeID(), t.getRoomTypeName(), t.getTypeDescription(), t.getTypeNote()))
            .collect(Collectors.toList());
    }

    private RoomDto toDto(RoomInformation entity) {
        return new RoomDto(
            entity.getRoomID(),
            entity.getRoomNumber(),
            entity.getRoomDetailDescription(),
            entity.getRoomMaxCapacity(),
            entity.getRoomType() != null ? entity.getRoomType().getRoomTypeID() : null,
            entity.getRoomType() != null ? entity.getRoomType().getRoomTypeName() : null,
            entity.getRoomStatus(),
            entity.getRoomPricePerDay()
        );
    }
}
