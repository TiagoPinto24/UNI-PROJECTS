package com.test.hotelbay.Hotel.HotelControlers;

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
import com.test.hotelbay.Hotel.HotelDTOs.HotelDTO;

@RestController
@RequestMapping("/hotelbay/hotel/changeInfo")
public class HotelChangeInfoControler {
    private final AccountRepository accountRepository;
    private final HotelRepository hotelRepository;
    public HotelChangeInfoControler (AccountRepository accountRepository, HotelRepository hotelRepository){
        this.accountRepository = accountRepository;
        this.hotelRepository = hotelRepository;
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<?> changeInfoHotel(@PathVariable Long hotelId, @RequestHeader("X-ADMIN-ID") Long adminId, @RequestBody HotelDTO request) {
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
        HotelDTO response = request;
        
        if (request.getName() != null) {
            response.setName(request.getName());
            hotel.setName(request.getName());
        }
        if (request.getDescription() != null) {
            response.setDescription(request.getDescription());
            hotel.setDescription(request.getDescription());
        }
        if (request.getLocation() != null) {
            response.setLocation(request.getLocation()); 
            hotel.setLocation(request.getLocation());
        }
        if (request.getContact() != null) {
            response.setContact(request.getContact());
            hotel.setContact(request.getContact());
        }
        if (request.getActive() != null) {
            response.setActive(request.getActive());
            hotel.setActive(request.getActive());
        }
        if (request.getComodities() != null) {
            response.setComodities(request.getComodities());
            hotel.setComodities(request.getComodities());
        }
        if (request.getPolicies() != null) {
            response.setPolicies(request.getPolicies());
            hotel.setPolicies(request.getPolicies());
        }
        if (request.getAdminList() != null) {
            response.setAdminList(request.getAdminList());
            hotel.setAdminList(request.getAdminList());
        }
        if (request.getRoomList() != null) {
            response.setRoomList(request.getRoomList());
            hotel.setRoomList(request.getRoomList());
        }

        hotelRepository.save(hotel);
        return ResponseEntity.ok(response);
    }
}
