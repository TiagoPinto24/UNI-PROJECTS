package com.test.hotelbay.Account.AccountControlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;
import com.test.hotelbay.Account.AccountType;
import com.test.hotelbay.Account.AccountDTOs.AccountRequestDTO;
import com.test.hotelbay.Account.AccountDTOs.AccountResponseDTO;

@RestController
@RequestMapping("/hotelbay/account/register")
public class AccountRegisterControler {

    private final AccountRepository accountRepository;
    public AccountRegisterControler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping("/user")
    public ResponseEntity<?> register(@RequestBody AccountRequestDTO request) {
        Account newAccount = new Account(request.getEmail(), request.getName(), request.getUsername(), request.getPassword(), AccountType.GUEST);
        accountRepository.save(newAccount);

        AccountResponseDTO response = new AccountResponseDTO(newAccount.getUsername(), newAccount.getEmail(), newAccount.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody AccountRequestDTO request) {
        Account newAccount = new Account(request.getEmail(), request.getName(), request.getUsername(), request.getPassword(), AccountType.ADMIN);
        accountRepository.save(newAccount);

        AccountResponseDTO response = new AccountResponseDTO(newAccount.getUsername(), newAccount.getEmail(), newAccount.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

