package fu.edu.sba301.assignment03.controller;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.RoomDto;
import fu.edu.sba301.assignment03.dto.RoomTypeDto;
import fu.edu.sba301.assignment03.service.IRoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    private final IRoomService roomService;

    public RoomController(IRoomService roomService) {
        this.roomService = roomService;
    }

    // Public endpoint: "Do not need authentication to view the room information in this system."
    @GetMapping
    public ResponseEntity<List<RoomDto>> getRooms(
        @RequestParam(defaultValue = "false") boolean all,
        @RequestParam(required = false) String keyword
    ) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return ResponseEntity.ok(roomService.searchRooms(keyword));
        }
        if (all) {
            return ResponseEntity.ok(roomService.getAllRooms());
        }
        return ResponseEntity.ok(roomService.getActiveRooms());
    }

    @GetMapping("/public")
    public ResponseEntity<List<RoomDto>> getPublicRooms() {
        return ResponseEntity.ok(roomService.getActiveRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody RoomDto dto) {
        RoomDto created = roomService.createRoom(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable Integer id, @Valid @RequestBody RoomDto dto) {
        RoomDto updated = roomService.updateRoom(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
