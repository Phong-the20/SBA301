package fu.edu.sba301.assignment03.entity;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "RoomInformation")
public class RoomInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomID;

    @NotBlank(message = "Room number is required")
    @Column(nullable = false, unique = true, length = 50)
    private String roomNumber;

    @Lob
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String roomDetailDescription;

    @NotNull(message = "Max capacity is required")
    @Min(value = 1, message = "Max capacity must be at least 1")
    @Column(nullable = false)
    private Integer roomMaxCapacity;

    @NotNull(message = "Room type is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roomTypeID", nullable = false)
    private RoomType roomType;

    @NotNull(message = "Room status is required (1: Active, 0: Inactive/Deleted)")
    @Column(nullable = false)
    private Integer roomStatus = 1;

    @NotNull(message = "Price per day is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price per day must be greater than 0")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal roomPricePerDay;

    public RoomInformation() {
    }

    public RoomInformation(Integer roomID, String roomNumber, String roomDetailDescription, Integer roomMaxCapacity, RoomType roomType, Integer roomStatus, BigDecimal roomPricePerDay) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.roomDetailDescription = roomDetailDescription;
        this.roomMaxCapacity = roomMaxCapacity;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.roomPricePerDay = roomPricePerDay;
    }

    public Integer getRoomID() {
        return roomID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomDetailDescription() {
        return roomDetailDescription;
    }

    public void setRoomDetailDescription(String roomDetailDescription) {
        this.roomDetailDescription = roomDetailDescription;
    }

    public Integer getRoomMaxCapacity() {
        return roomMaxCapacity;
    }

    public void setRoomMaxCapacity(Integer roomMaxCapacity) {
        this.roomMaxCapacity = roomMaxCapacity;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Integer getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    public BigDecimal getRoomPricePerDay() {
        return roomPricePerDay;
    }

    public void setRoomPricePerDay(BigDecimal roomPricePerDay) {
        this.roomPricePerDay = roomPricePerDay;
    }
}
