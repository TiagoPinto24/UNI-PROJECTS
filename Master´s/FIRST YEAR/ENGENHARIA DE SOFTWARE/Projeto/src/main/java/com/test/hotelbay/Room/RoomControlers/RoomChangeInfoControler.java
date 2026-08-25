package com.test.hotelbay.Room.RoomControlers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;
import com.test.hotelbay.Account.AccountType;
import com.test.hotelbay.Hotel.Hotel;
import com.test.hotelbay.Hotel.HotelRepository;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;
import com.test.hotelbay.Room.RoomDTOs.RoomRequestDTO;
import com.test.hotelbay.Room.RoomDTOs.RoomResponseDTO;

@RestController
@RequestMapping("/hotelbay/room/changeInfo")
public class RoomChangeInfoControler {
    private final AccountRepository accountRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    public RoomChangeInfoControler (AccountRepository accountRepository, HotelRepository hotelRepository, RoomRepository roomRepository){
        this.accountRepository = accountRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<?> changeInfoRoom(@PathVariable Long roomId, @RequestHeader("X-ADMIN-ID") Long adminId, @RequestHeader("X-HOTEL-ID") Long hotelId, @RequestBody RoomRequestDTO request) {
        Optional<Account> OptionalAdmin = accountRepository.findById(adminId);
        if (OptionalAdmin.isEmpty() || OptionalAdmin.get().getType() != AccountType.ADMIN ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This account was not found as an admin");
        }
        Account admin = OptionalAdmin.get();

        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
        if (hotelOptional.isEmpty() || !hotelOptional.get().getAdminList().contains(admin)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("An hotel with this ID was not found under the manegement of this admin");
        }
        Hotel hotel = hotelOptional.get();

        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isEmpty() || roomOptional.get().getHotel() != hotel) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The room ith the given ID was not found in the given hotel");
        }
        Room room = roomOptional.get();
        RoomResponseDTO response = new RoomResponseDTO(room.getNumber(), room.getDescription(), room.getCapacity(), room.getPrice(), room.getStatus(), room.getCategories(), room.getHotel().getId());

        if (request.getNumber() != null) {
            response.setNumber(request.getNumber());
            room.setNumber(request.getNumber());
        }
        if (request.getDescription() != null) {
            response.setDescription(request.getDescription());
            room.setDescription(request.getDescription());
        }
        if (request.getCapacity() != 0) {
            response.setCapacity(request.getCapacity()); 
            room.setCapacity(request.getCapacity());
        }
        if (request.getPrice() != null) {
            response.setPrice(request.getPrice());
            room.setPrice(request.getPrice());
        }
        if (request.getStatus() != null) {
            response.setStatus(request.getStatus());
            room.setStatus(request.getStatus());
        }
        if (request.getCategories() != null) {
            response.setCategories(request.getCategories());
            room.setCategories(request.getCategories());
        }
        roomRepository.save(room);
        return ResponseEntity.ok(response);
    }
}
