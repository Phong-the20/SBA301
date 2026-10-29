package fu.edu.sba301.assignment03.entity;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RoomType")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomTypeID;

    @NotBlank(message = "Room type name is required")
    @Column(nullable = false, length = 100)
    private String roomTypeName;

    @Column(length = 250)
    private String typeDescription;

    @Column(length = 250)
    private String typeNote;

    @JsonIgnore
    @OneToMany(mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<RoomInformation> rooms = new ArrayList<>();

    public RoomType() {
    }

    public RoomType(Integer roomTypeID, String roomTypeName, String typeDescription, String typeNote) {
        this.roomTypeID = roomTypeID;
        this.roomTypeName = roomTypeName;
        this.typeDescription = typeDescription;
        this.typeNote = typeNote;
    }

    public Integer getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(Integer roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getTypeNote() {
        return typeNote;
    }

    public void setTypeNote(String typeNote) {
        this.typeNote = typeNote;
    }

    public List<RoomInformation> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomInformation> rooms) {
        this.rooms = rooms;
    }
}
