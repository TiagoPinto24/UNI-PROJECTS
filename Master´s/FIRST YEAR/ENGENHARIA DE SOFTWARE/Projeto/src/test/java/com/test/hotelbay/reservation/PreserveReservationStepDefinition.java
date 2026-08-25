package com.test.hotelbay.reservation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.StepDefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PreserveReservationStepDefinition extends StepDefinition {

    @Autowired
    private MockMvc mvc;

    private ResultActions action;

    private Integer guestId;

    @Given("the preserve reservation service URL")
    public void the_preserve_reservation_service_url() {
    }

    @Given("a guest with past reservations exists")
    public void a_guest_with_past_reservations_exists() {
        this.guestId = 1;
    }

    @When("^the client calls GET /guests/(\\d+)/reservations$")
    public void the_client_calls_get_guests_reservations(String guestId) throws Exception {
        Integer id = Integer.valueOf(guestId);

        action = mvc.perform(get("/guests/{guestId}/reservations", id)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Then("the preserve reservation client receives status code {int}")
    public void the_preserve_reservation_client_receives_status_code(
            Integer statusCode) throws Exception {

        action.andExpect(
                status().is(statusCode));
    }

    @Then("the response contains a list of past reservations")
    public void the_response_contains_reservations() throws Exception {
        action.andExpect(jsonPath("$").isArray());
        action.andExpect(jsonPath("$.length()").value(Matchers.greaterThan(0)));
    }
}