package com.test.hotelbay.general.helpers;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;
import com.test.hotelbay.Category.Category;
import com.test.hotelbay.Category.CategoryRepository;
import com.test.hotelbay.Hotel.Hotel;
import com.test.hotelbay.Hotel.HotelRepository;

@Service
public class DeleteAccountHelper {
    private final AccountRepository accountRepository;
    private final HotelRepository hotelRepository;
    private final CategoryRepository categoryRepository;
    private final DeleteCategoryHelper deleteHelper;

    public DeleteAccountHelper(AccountRepository accountRepository, HotelRepository hotelRepository, CategoryRepository categoryRepository, DeleteCategoryHelper deleteHelper) {
        this.accountRepository = accountRepository;
        this.hotelRepository = hotelRepository;
        this.categoryRepository = categoryRepository;
        this.deleteHelper = deleteHelper;
    }

    @Transactional
    public void deleteAccount(String accountEmail) {

        Optional<Account> accountOptional = accountRepository.findByEmail(accountEmail);
        if (accountOptional.isEmpty()) {
            fail("TEST ERROR: Admin account was not created before this step");
        } else {
        
            Account account = accountOptional.get();
        
            List<Hotel> hotels = hotelRepository.findByAdminListContains(account);
            List<Category> categories = categoryRepository.findByAdmin(account);

            for (Hotel hotel : hotels) {
                hotel.getAdminList().remove(account);
            }

            for (Category category : categories) {
                deleteHelper.deleteCategory(category);
                categoryRepository.delete(category);
            }

            hotelRepository.saveAll(hotels);

            accountRepository.delete(account);
        }
    }
}
