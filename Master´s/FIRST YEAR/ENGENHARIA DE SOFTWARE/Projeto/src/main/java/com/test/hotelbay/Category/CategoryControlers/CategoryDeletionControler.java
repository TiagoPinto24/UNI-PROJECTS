package com.test.hotelbay.Category.CategoryControlers;

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
import com.test.hotelbay.Category.Category;
import com.test.hotelbay.Category.CategoryRepository;

@RestController
@RequestMapping("/hotelbay/category/delete")
public class CategoryDeletionControler {
    private AccountRepository accountRepository;
    private CategoryRepository categoryRepository;
    public CategoryDeletionControler(AccountRepository accountRepository, CategoryRepository categoryRepository) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId, @RequestHeader("X-ADMIN-ID") Long adminId) {
        Optional<Account> OptionalAdmin = accountRepository.findById(adminId);
        if (OptionalAdmin.isEmpty() || OptionalAdmin.get().getType() != AccountType.ADMIN ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This account was not found as an admin");
        }

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty() || categoryOptional.get().getAdmin().getId() != adminId) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This category was not found under the manegement of the admin given");
        }
        Category category = categoryOptional.get();
        categoryRepository.delete(category);

        return ResponseEntity.ok().build();
    }
}
