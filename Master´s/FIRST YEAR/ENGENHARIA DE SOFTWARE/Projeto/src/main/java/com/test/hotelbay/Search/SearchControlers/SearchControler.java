package com.test.hotelbay.Search.SearchControlers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.hotelbay.Reservation.ReservationRepository;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;
import com.test.hotelbay.Room.RoomDTOs.RoomResponseDTO;

@RestController
@RequestMapping("/hotelbay/search")
public class SearchControler {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public SearchControler(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/rooms")
    public ResponseEntity<?> searchRooms(
            @RequestParam(required = false) String hotelName,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) BigDecimal maximumPrice,
            @RequestParam(required = false) BigDecimal minimumPrice,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkout,
            @RequestHeader("X-USER-ID") Long userId) {

        List<Room> matchingRooms = roomRepository.searchRooms(hotelName, location, categoryName, maximumPrice,
                minimumPrice, capacity);

        if (checkin != null || checkout != null) {
            List<Room> AvailableRooms = reservationRepository.searchAvailableRooms(checkin, checkout);

            List<Room> resultingRooms = new ArrayList<>();

            for (Room room : AvailableRooms) {
                if (matchingRooms.contains(room)) {
                    resultingRooms.add(room);
                }
            }
            List<RoomResponseDTO> results = resultingRooms.stream()
                    .map(r -> new RoomResponseDTO(r.getNumber(), r.getDescription(), r.getCapacity(), r.getPrice(),
                            r.getStatus(), r.getCategories(), r.getHotel().getId()))
                    .toList();

            for (Room room : matchingRooms) {
                System.out.println("\n\n\n\n\n" + room.getNumber());

            }

            Map<String, Object> response = new HashMap<>();
            response.put("rooms", results);

            return ResponseEntity.ok(response);
        } else {
            List<RoomResponseDTO> results = matchingRooms.stream()
                    .map(r -> new RoomResponseDTO(r.getNumber(), r.getDescription(), r.getCapacity(), r.getPrice(),
                            r.getStatus(), r.getCategories(), r.getHotel().getId()))
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("rooms", results);

            return ResponseEntity.ok(response);
        }
    }
}
