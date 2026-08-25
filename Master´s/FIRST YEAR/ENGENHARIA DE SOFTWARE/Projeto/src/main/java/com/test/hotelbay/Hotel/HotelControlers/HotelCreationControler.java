package com.test.hotelbay.Hotel.HotelControlers;

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
import com.test.hotelbay.Hotel.HotelDTOs.HotelDTO;

@RestController
@RequestMapping("/hotelbay/hotel")
public class HotelCreationControler {
    private final AccountRepository accountRepository;
    private final HotelRepository hotelRepository;
    public HotelCreationControler (AccountRepository accountRepository, HotelRepository hotelRepository){
        this.accountRepository = accountRepository;
        this.hotelRepository = hotelRepository;
    }
    

    @PostMapping("/create")
    public ResponseEntity<?> createHotel(@RequestHeader("X-ADMIN-ID") Long adminId,  @RequestBody HotelDTO request) {
        Optional<Account> OptionalAdmin = accountRepository.findById(adminId);
        if (OptionalAdmin.isEmpty() || OptionalAdmin.get().getType() != AccountType.ADMIN ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This account was not found as an admin");
        }

        Hotel newHotel = new Hotel(request.getName(), request.getDescription(), request.getLocation(), request.getContact(), request.getComodities(), request.getPolicies(), request.getAdminList(), request.getRoomList());
        hotelRepository.save(newHotel);

        HotelDTO response = new HotelDTO(newHotel.getName(), newHotel.getDescription(), newHotel.getLocation(), newHotel.getContact(), newHotel.getActive(), newHotel.getComodities(), newHotel.getPolicies(), newHotel.getAdminList(), newHotel.getRoomList());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
