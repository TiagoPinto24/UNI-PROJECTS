package com.test.hotelbay.account;

import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccountDelitionStepDefinition extends StepDefinition{
    private final ScenarioContext context;

    public AccountDelitionStepDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepository accountRepository;

    
    @When("the client calls \\/account\\/delete\\/me")
    public void when_the_client_calls_account_delete() throws Exception {
        ResultActions action = mvc.perform(delete(context.getUrl() + "/account/delete/me")
            .header("X-USER-ID", String.valueOf(context.getExistingAccount().getId()))
            .contentType(MediaType.APPLICATION_JSON));
        context.setResultActions(action);
    }

    @Then("the account is successfully deleted")
    public void and_the_account_is_successfully_deleted() throws Exception{
        Optional<Account> accountOptional = accountRepository.findByEmail(context.getExistingAccount().getEmail());
        assertFalse(accountOptional.isPresent());
    }
}