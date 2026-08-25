package com.test.hotelbay.Hotel.HotelControlers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;
import com.test.hotelbay.Account.AccountType;
import com.test.hotelbay.Hotel.Hotel;
import com.test.hotelbay.Hotel.HotelRepository;

@RestController
@RequestMapping("/hotelbay/hotel/delete")
public class HotelDeletionControler {
    private final AccountRepository accountRepository;
    private final HotelRepository hotelRepository;
    public HotelDeletionControler (AccountRepository accountRepository, HotelRepository hotelRepository){
        this.accountRepository = accountRepository;
        this.hotelRepository = hotelRepository;
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<?> deleteHotel(@PathVariable Long hotelId, @RequestHeader("X-ADMIN-ID") Long adminId) {
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
        hotelRepository.delete(hotel);

        return ResponseEntity.ok().build();

    }
}
