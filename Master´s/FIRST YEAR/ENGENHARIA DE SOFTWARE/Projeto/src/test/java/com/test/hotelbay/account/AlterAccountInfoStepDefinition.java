package com.test.hotelbay.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AlterAccountInfoStepDefinition extends StepDefinition{
    private final ScenarioContext context;

    public AlterAccountInfoStepDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    String requestBody;

    String newUserEmail = "newtest@email.com";

    @When("the client gives valid information to alter the account")
    public void when_the_client_gives_valid_information_to_alter_the_account() throws Exception {

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", newUserEmail);

        requestBody = objectMapper.writeValueAsString(payload);
    }

    @When("calls \\/account\\/changeInfo\\/me")
    public void and_calls_account_changeInfo_me() throws Exception{
        ResultActions action = mvc.perform(put(context.getUrl() + "/account/changeInfo/me")
            .header("X-USER-ID", String.valueOf(context.getExistingAccount().getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));
        context.setResultActions(action);
    }

    @Then("the informtaion is successfully changed")
    public void and_the_information_is_successfully_changed() {
        Optional<Account> accountOptional = accountRepository.findByEmail(newUserEmail);
        assertTrue(accountOptional.isPresent());

        Account account = accountOptional.get();
        assertEquals("newtest@email.com", account.getEmail());   
        assertEquals("John Doe", account.getName());   
        assertEquals("JohnDoe123", account.getUsername());   
    }

}
