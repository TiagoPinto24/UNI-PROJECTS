package com.test.hotelbay.general;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;
import com.test.hotelbay.Account.AccountType;
import com.test.hotelbay.Hotel.Hotel;
import com.test.hotelbay.Hotel.HotelRepository;
import com.test.hotelbay.general.helpers.DeleteAccountHelper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class generalStepsDefinition  extends StepDefinition{
    private final ScenarioContext context;
    private final DeleteAccountHelper deleteHelper;

    public generalStepsDefinition(ScenarioContext context, DeleteAccountHelper helper) {
        this.context = context;
        this.deleteHelper = helper;
    }
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private HotelRepository hotelRepository;

    private

    Account existingAccount = new Account("test@email.com", "John Doe", "JohnDoe123", "Password123", AccountType.GUEST);
    Account existingAdminAccount = new Account("admin@email.com", "Admin Lopez", "AdminLopez456", "Password123", AccountType.ADMIN);
    Hotel existingHotel = new Hotel("Grand Hotel", "A 4 star hotel situated in the middle of Lisbon", "Lisbon", "grandhotel@email.com", 
        new ArrayList<>(List.of("spa", "pool", "restaurant")), new ArrayList<>(List.of()), new ArrayList<>(List.of()), new ArrayList<>());

    @Given("the service URL")
    public void given_the_service_url() {
        context.setUrl("/hotelbay");
    }
    
    @Transactional
    @Given("the current client has an account")
    public void and_the_current_client_has_an_account() {
        if(accountRepository.findByEmail(existingAccount.getEmail()).isEmpty()) {
            accountRepository.save(existingAccount);
        } else {
            accountRepository.deleteByEmail(existingAccount.getEmail());
            accountRepository.save(existingAccount);
        }
        context.setExistingAccount(existingAccount);
    }

    @Transactional
    @Given("the current client has a valid administrator account")
    public void and_the_current_client_has_a_valid_administrator_account() {
        if (accountRepository.findByEmail(existingAdminAccount.getEmail()).isEmpty()) {
            accountRepository.save(existingAdminAccount);
        } else {
            deleteHelper.deleteAccount(existingAdminAccount.getEmail());
            accountRepository.deleteByEmail(existingAdminAccount.getEmail());
            accountRepository.save(existingAdminAccount);
        }
        context.setExistingAdminAccount(existingAdminAccount);
    }

    @Transactional
    @Given("a hotel with the name Grand Hotel wich is managed by the current client")
    public void and_a_hotel_with_the_name_grand_hotel_wich_is_menaged_by_the_current_client() {
        Account admin = accountRepository.findById(
            context.getExistingAdminAccount().getId()
        ).orElseThrow();

        existingHotel.getAdminList().add(admin);
        if (hotelRepository.findByName(existingHotel.getName()).isEmpty()) {
            hotelRepository.save(existingHotel);
        } else {
            hotelRepository.deleteByName("Grand Hotel");
            hotelRepository.save(existingHotel);
        }
        context.setExistingHotel(existingHotel);
    }

    @Then("the client recieves code 200")
    public void then_the_client_recieves_code_200() throws Exception{
        context.getResultActions().andExpect(status().isOk());
    }

    @Then("the client recieves code 201")
    public void then_the_client_recieves_code_201() throws Exception{
        context.getResultActions().andExpect(status().isCreated());
    }
}
