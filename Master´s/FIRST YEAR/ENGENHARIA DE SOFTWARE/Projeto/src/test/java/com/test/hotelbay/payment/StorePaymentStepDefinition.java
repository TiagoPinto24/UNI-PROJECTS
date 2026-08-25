package com.test.hotelbay.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.reviews.ReviewTestContext;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StorePaymentStepDefinition extends StepDefinition {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ReviewTestContext context;

    private Integer reservationId;

    @Given("a reservation with past payments exists")
    public void a_reservation_with_past_payments_exists() {
        this.reservationId = 1;
    }

    @When("the client calls payments with {int}")
    public void the_client_calls_get_payments(Integer reservationId) throws Exception {

        ResultActions result = mvc.perform(
                get("/payments/{reservationId}", reservationId)
                        .contentType(MediaType.APPLICATION_JSON));

        context.setAction(result);
    }

    @Then("the response contains a list of payments")
    public void the_response_contains_a_list_of_payments() throws Exception {

        context.getAction()
                .andExpect(jsonPath("$").isArray());
    }
}