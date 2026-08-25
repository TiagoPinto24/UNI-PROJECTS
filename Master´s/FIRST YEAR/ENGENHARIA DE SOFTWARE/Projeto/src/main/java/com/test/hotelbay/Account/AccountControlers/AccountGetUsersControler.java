package com.test.hotelbay.Account.AccountControlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;
import com.test.hotelbay.Account.AccountDTOs.AccountResponseDTO;

@RestController
@RequestMapping("/hotelbay/account")
public class AccountGetUsersControler {
    private final AccountRepository accountRepository;
    public AccountGetUsersControler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        
        List<AccountResponseDTO> response = new ArrayList<>();
        for (Account account : accounts) {
            response.add(new AccountResponseDTO(account.getUsername(), account.getEmail(), account.getName()));
        }

        return ResponseEntity.ok(response);
    }
}
