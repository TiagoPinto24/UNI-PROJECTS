package com.test.hotelbay.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class NewAccountStepDefenition extends StepDefinition{
    private final ScenarioContext context;

    public NewAccountStepDefenition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    String requestBody;

    String newUserEmail = "test@email.com";

    @Transactional
    @Given("the current client does not have an account")
    public void and_the_current_client_does_not_have_an_account() {
        accountRepository.deleteByEmail(newUserEmail);
    }

    @When("the client gives valid information to create a new account")
    public void when_the_client_gives_valid_information_to_create_a_new_account() throws Exception {

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", newUserEmail);
        payload.put("password", "Password123");
        payload.put("name", "John Doe");
        payload.put("username", "JohnDoe123");

        requestBody = objectMapper.writeValueAsString(payload);
    }

    @When("calls \\/account\\/register\\/user")
    public void and_calls_account_register() throws Exception {
        ResultActions action = mvc.perform(post(context.getUrl() + "/account/register/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        context.setResultActions(action);
    }

    @Then("a new account is created")
    public void and_a_new_account_is_created() throws Exception{
        Optional<Account> accountOptional = accountRepository.findByEmail("test@email.com");
        assertTrue(accountOptional.isPresent());

        Account account = accountOptional.get();
        assertEquals("test@email.com", account.getEmail());   
        assertEquals("John Doe", account.getName());   
        assertEquals("JohnDoe123", account.getUsername());   
    }
}