package com.test.hotelbay.Account.AccountControlers;

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

@RestController
@RequestMapping("/hotelbay/account/delete")
public class AccountDelitionController {
    private final AccountRepository accountRepository;
    public AccountDelitionController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteOwnAccount(@RequestHeader("X-USER-ID") Long userId) {
        Optional<Account> OptionalUser = accountRepository.findById(userId);
        if (OptionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        Account user = OptionalUser.get();
        accountRepository.delete(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteOtherAccount(@PathVariable Long userId, @RequestHeader("X-ADMIN-ID") Long adminId) {
        Optional<Account> OptionalAdmin = accountRepository.findById(adminId);
        if (OptionalAdmin.isEmpty() || OptionalAdmin.get().getType() != AccountType.ADMIN ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This account was not found as an admin");
        }

        Optional<Account> OptionalUser = accountRepository.findById(userId);
        if (OptionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Account user = OptionalUser.get();
        accountRepository.delete(user);
        return ResponseEntity.ok().build();
    }
}
