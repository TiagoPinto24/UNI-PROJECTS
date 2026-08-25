package com.test.hotelbay.Category.CategoryControlers;

import java.util.ArrayList;
import java.util.List;
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
import com.test.hotelbay.Category.Category;
import com.test.hotelbay.Category.CategoryRepository;
import com.test.hotelbay.Category.CategoryDTOs.CategoryRequestDTO;
import com.test.hotelbay.Category.CategoryDTOs.CategoryResponseDTO;

@RestController
@RequestMapping("/hotelbay/category")
public class CategoryCreationControler {
    private AccountRepository accountRepository;
    private CategoryRepository categoryRepository;
    public CategoryCreationControler(AccountRepository accountRepository, CategoryRepository categoryRepository) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestHeader("X-ADMIN-ID") Long adminId,  @RequestBody CategoryRequestDTO request) {
        Optional<Account> OptionalAdmin = accountRepository.findById(adminId);
        if (OptionalAdmin.isEmpty() || OptionalAdmin.get().getType() != AccountType.ADMIN ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This account was not found as an admin");
        }
        Account admin = OptionalAdmin.get();
        Category newCategory = new Category(admin,request.getName());
        CategoryResponseDTO response = new CategoryResponseDTO(request.getName(),admin.getId());

        if (request.getSubCategory() != null && !request.getSubCategory().isEmpty()) {
            newCategory.setSubCategories(request.getSubCategory());
            List<Long> subCategoriesIds = new ArrayList<>();
            for (Category category : request.getSubCategory()) {
                subCategoriesIds.add(category.getId());
            }
            response.setSubCategoryIds(subCategoriesIds);
        }
        if (request.getSuperCategory() != null) {
            newCategory.setSuperCategory(request.getSuperCategory());
            response.setSuperCategoryId(request.getSuperCategory().getId());
        }

        categoryRepository.save(newCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
