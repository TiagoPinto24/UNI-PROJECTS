package com.test.hotelbay.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;
import com.test.hotelbay.Account.AccountType;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminALterUserInfoStepDefinition extends StepDefinition{
    private final ScenarioContext context;

    public AdminALterUserInfoStepDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;
    
    String requestBody;

    Account existingUserAccount = new Account("user@email.com", "John Doe", "JohnDoe123", "Password456", AccountType.GUEST);
    String newUserEmail = "newuser@email.com";

    @Transactional
    @Given("an account with the email user@email.com exists")
    public void and_an_account_with_the_email_exists() {
        if (accountRepository.findByEmail("user@email.com").isEmpty()) {
            accountRepository.save(existingUserAccount);
        } else {
            accountRepository.deleteByEmail("user@email.com");
            accountRepository.save(existingUserAccount);
        }
        context.setExistingAccount(existingUserAccount);
    }

    @When("the clients gives valid information to alter the user account")
    public void when_the_client_gives_valid_information_to_alter_the_user_account() throws Exception {

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", newUserEmail);

        requestBody = objectMapper.writeValueAsString(payload);
    }

    @When("the client calls \\/account\\/changeInfo\\/user")
    public void and_calls_account_changeInfo_user() throws Exception{
        String idAdmin = String.valueOf(context.getExistingAdminAccount().getId());
        String idUser = String.valueOf(context.getExistingAccount().getId());

        ResultActions action = mvc.perform(put(context.getUrl() + "/account/changeInfo/user/" + idUser)
            .header("X-ADMIN-ID", idAdmin)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));
        context.setResultActions(action);
    }

    @Then("the info of the respective account is changed")
    public void and_the_info_of_the_respective_account_is_changed() {
        Optional<Account> accountOptional = accountRepository.findByEmail(newUserEmail);
        assertTrue(accountOptional.isPresent());

        Account account = accountOptional.get();
        assertEquals(newUserEmail, account.getEmail());   
        assertEquals("John Doe", account.getName());   
        assertEquals("JohnDoe123", account.getUsername());
    }

}
