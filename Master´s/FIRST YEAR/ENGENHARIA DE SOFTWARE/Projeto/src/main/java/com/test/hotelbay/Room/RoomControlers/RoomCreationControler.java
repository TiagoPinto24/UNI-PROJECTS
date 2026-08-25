package com.test.hotelbay.Room.RoomControlers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/hotelbay/room")
public class RoomCreationControler {
    private final AccountRepository accountRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    public RoomCreationControler (AccountRepository accountRepository, HotelRepository hotelRepository, RoomRepository roomRepository){
        this.accountRepository = accountRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestHeader("X-ADMIN_ID") Long adminId, @RequestHeader("X-HOTEL-ID") Long hotelId, @RequestBody RoomRequestDTO request) {
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

        Room newRoom = new Room(request.getNumber(), request.getDescription(), request.getCapacity(), request.getPrice(), request.getStatus(), request.getCategories(),hotel);
        roomRepository.save(newRoom);
        RoomResponseDTO response = new RoomResponseDTO(newRoom.getNumber(), newRoom.getDescription(), newRoom.getCapacity(),newRoom.getPrice(), newRoom.getStatus(), newRoom.getCategories(), newRoom.getHotel().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
