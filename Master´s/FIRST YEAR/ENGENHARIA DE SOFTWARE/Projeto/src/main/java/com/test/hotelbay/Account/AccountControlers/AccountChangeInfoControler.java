package com.test.hotelbay.Account.AccountControlers;

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
import com.test.hotelbay.Account.AccountDTOs.AccountRequestDTO;
import com.test.hotelbay.Account.AccountDTOs.AccountResponseDTO;

@RestController
@RequestMapping("/hotelbay/account/changeInfo")
public class AccountChangeInfoControler {
    private final AccountRepository accountRepository;
    public AccountChangeInfoControler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PutMapping("/me")
    public ResponseEntity<?> changeInfoOwnAccount(@RequestHeader("X-USER-ID") Long userID, @RequestBody AccountRequestDTO request) {
        Optional<Account> OptionalUser = accountRepository.findById(userID);
        if (OptionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        Account user = OptionalUser.get();
        AccountResponseDTO response = new AccountResponseDTO(user.getUsername(), user.getEmail(), user.getName());

        if (request.getEmail() != null) {
            response.setEmail(request.getEmail());
            user.setEmail(request.getEmail());
        }
        if (request.getName() != null) {
            response.setName(request.getName());
            user.setName(request.getName());
        }
        if (request.getUsername() != null) {
            response.setUsername(request.getUsername()); 
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) user.setPassword(request.getPassword());
        accountRepository.save(user);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<?> changeInfoOtherAccount(@PathVariable Long userId, @RequestHeader("X-ADMIN-ID") Long adminId, @RequestBody AccountRequestDTO request) {
        Optional<Account> OptionalAdmin = accountRepository.findById(adminId);
        if (OptionalAdmin.isEmpty() || OptionalAdmin.get().getType() != AccountType.ADMIN ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This account was not found as an admin");
        }

        Optional<Account> OptionalUser = accountRepository.findById(userId);
        if (OptionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Account user = OptionalUser.get();
        AccountResponseDTO response = new AccountResponseDTO(user.getUsername(), user.getEmail(), user.getName());

        if (request.getEmail() != null) {
            response.setEmail(request.getEmail());
            user.setEmail(request.getEmail());
        }
        if (request.getName() != null) {
            response.setName(request.getName());
            user.setName(request.getName());
        }
        if (request.getUsername() != null) {
            response.setUsername(request.getUsername()); 
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) user.setPassword(request.getPassword());
        accountRepository.save(user);

        return ResponseEntity.ok(response);
    }
}
